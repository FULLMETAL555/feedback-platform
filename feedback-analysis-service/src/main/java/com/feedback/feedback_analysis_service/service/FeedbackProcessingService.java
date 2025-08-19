package com.feedback.feedback_analysis_service.service;

import com.feedback.feedback_analysis_service.client.FeedbackServiceClient;
import com.feedback.feedback_analysis_service.dto.FeedbackDTO;
import com.feedback.feedback_analysis_service.model.Category;
import com.feedback.feedback_analysis_service.model.CategorySummary;
import com.feedback.feedback_analysis_service.model.Product;
import com.feedback.feedback_analysis_service.repository.CategoryRepository;
import com.feedback.feedback_analysis_service.repository.CategorySummaryRepository;
import com.feedback.feedback_analysis_service.repository.ProductRepository;
import com.feedback.feedback_analysis_service.util.AIUtils;
import com.feedback.feedback_analysis_service.util.CategoryGenerationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackProcessingService {

    private final FeedbackServiceClient feedbackServiceClient;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategorySummaryRepository categorySummaryRepository;
    private final AIUtils aiUtils;
    private final CategoryGenerationUtil categoryGenerationUtil;

    private static final double UNCLASSIFIED_THRESHOLD = 0.10;

    /**
     * Main entry point: Processes all feedback for a given product.
     */
    @Transactional
    public void processFeedbackForProduct(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) return;
        Product product = productOpt.get();

        // Get feedbacks for this product from FeedbackService
        List<FeedbackDTO> allFeedbacks = feedbackServiceClient.getFeedbacksForProduct(productId);

        if (allFeedbacks.size() < 4) return; // Insufficient sample size

        List<Category> existingCategories = categoryRepository.findByProductId(productId);

        if (existingCategories.isEmpty()) {
            processInitialFeedbacks(product, allFeedbacks);
        } else {
            boolean sufficientCoverage = isCategoryCoverageSufficient(
                    existingCategories.stream().map(Category::getName).toList(),
                    allFeedbacks, UNCLASSIFIED_THRESHOLD
            );
            if (!sufficientCoverage) {
                log.info("Unclassified feedback exceeds threshold. Regenerating categories...");
                processCategoryRegeneration(product, allFeedbacks);
            } else {
                processIncrementalFeedbacks(product, existingCategories, allFeedbacks);
            }
        }
    }

    /**
     * Determines if existing categories cover feedback well enough (calls AI only for unclassified items)
     */
    private boolean isCategoryCoverageSufficient(List<String> existingCategoryNames, List<FeedbackDTO> feedbacks, double thresholdPercent) {
        long unclassifiedCount = feedbacks.stream()
                .filter(f -> f.getCategoryId() == null) // Only look at feedback not yet classified
                .filter(feedback -> {
                    String classified = aiUtils.classifyFeedbackIntoCategory(existingCategoryNames, feedback.getMessage());
                    return "No Match".equalsIgnoreCase(classified.trim());
                }).count();
        double unclassifiedRatio = (double) unclassifiedCount / feedbacks.size();
        log.info("Unclassified feedbacks: {}/{} = {}%", unclassifiedCount, feedbacks.size(), unclassifiedRatio * 100);
        return unclassifiedRatio <= thresholdPercent;
    }

    // Called only on first batch of feedbacks or after category regeneration
    private void processInitialFeedbacks(Product product, List<FeedbackDTO> feedbacks) {
        List<String> generatedCategories = categoryGenerationUtil.generateCategories(
                product.getName(),
                product.getDescription(),
                feedbacks.stream().map(FeedbackDTO::getMessage).toList()
        );
        List<Category> savedCategories = generatedCategories.stream()
                .map(categoryName -> {
                    Category category = new Category();
                    category.setProduct(product);
                    category.setName(categoryName);
                    return categoryRepository.save(category);
                })
                .collect(Collectors.toList());
        // Assign category and generate summaries
        generateAndSaveSummaries(product, savedCategories, feedbacks);
    }

    /**
     * Called only if not enough categories or coverage is poor (regenerating):
     * - Clears categoryId on all feedback for this product
     * - Deletes all categories and summaries for the product
     * - Generates new categories and summaries
     */
    private void processCategoryRegeneration(Product product, List<FeedbackDTO> allFeedbacks) {
        // Step 1: Remove (nullify) category assignments for all feedback of this product
        allFeedbacks.forEach(feedback -> {
            try {
                feedbackServiceClient.updateFeedbackCategory(feedback.getId(),null);
            } catch (Exception e) {
                log.warn("Failed to clear feedback categoryId for feedback {}: {}", feedback.getId(), e.getMessage());
            }
        });

        // Step 2: Remove old summaries and categories
        categorySummaryRepository.deleteAllByProductId(product.getId());
        categoryRepository.deleteByProductId(product.getId());

        // Step 3: Generate new categories using AI
        List<String> newCategoryNames = categoryGenerationUtil.generateCategories(
                product.getName(),
                product.getDescription(),
                allFeedbacks.stream().map(FeedbackDTO::getMessage).toList()
        );
        List<Category> newCategories = newCategoryNames.stream()
                .map(catName -> {
                    Category cat = new Category();
                    cat.setProduct(product);
                    cat.setName(catName);
                    return categoryRepository.save(cat);
                }).toList();

        // Step 4: Assign new categories and generate new summaries
        generateAndSaveSummaries(product, newCategories, allFeedbacks);
    }

    /**
     * Incrementally updates summaries & classifies new feedbacks only.
     * No repeated AI for already classified feedback.
     */
    private void processIncrementalFeedbacks(Product product, List<Category> existingCategories,
                                             List<FeedbackDTO> allFeedbacks) {
        List<CategorySummary> existingSummaries = categorySummaryRepository.findByProductId(product.getId());
        Map<Long, CategorySummary> summaryByCategoryId = existingSummaries.stream()
                .collect(Collectors.toMap(
                        summary -> summary.getCategory().getId(),
                        summary -> summary
                ));
        Map<String, Category> categoryMap = existingCategories.stream()
                .collect(Collectors.toMap(Category::getName, c -> c));

        Map<String, List<FeedbackDTO>> categorizedFeedbacks =
                classifyFeedbacksIntoCategories(existingCategories.stream().map(Category::getName).toList(),
                        allFeedbacks, categoryMap);

        for (Category category : existingCategories) {
            CategorySummary existingSummary = summaryByCategoryId.get(category.getId());
            List<FeedbackDTO> feedbacksForCategory = categorizedFeedbacks.getOrDefault(category.getName(), Collections.emptyList());
            updateOrCreateSummary(product, category, existingSummary, feedbacksForCategory);
        }
    }

    /**
     * Classifies feedbacks into categories:
     * - Feedbacks with existing categoryId are grouped directly.
     * - New/unclassified feedbacks are passed to AI, assigned a category, and the result is persisted in FeedbackService.
     */
    private Map<String, List<FeedbackDTO>> classifyFeedbacksIntoCategories(
            List<String> categories, List<FeedbackDTO> feedbacks, Map<String, Category> categoryMap) {
        Map<String, List<FeedbackDTO>> categorizedFeedbacks = new HashMap<>();
        categories.forEach(category -> categorizedFeedbacks.put(category, new ArrayList<>()));

        for (FeedbackDTO feedback : feedbacks) {
            String classifiedCategory = null;
            if (feedback.getCategoryId() != null) {
                // Find category from category ID
                Category cat = categoryMap.values().stream()
                        .filter(c -> c.getId().equals(feedback.getCategoryId()))
                        .findFirst().orElse(null);
                classifiedCategory = (cat != null) ? cat.getName() : null;
            } else {
                // Call AI for unclassified feedback
                classifiedCategory = aiUtils.classifyFeedbackIntoCategory(categories, feedback.getMessage()).trim();
                // Persist category ID if matched
                if (!"No Match".equalsIgnoreCase(classifiedCategory) && categoryMap.containsKey(classifiedCategory)) {
                    Long catId = categoryMap.get(classifiedCategory).getId();
                    feedback.setCategoryId(catId);
                    // Update the FeedbackService entry (assign/persist category)
                    try {
                        feedbackServiceClient.updateFeedbackCategory(feedback.getId(), catId);
                    } catch (Exception e) {
                        log.warn("Failed to persist categoryId to FeedbackService: {}", e.getMessage());
                    }
                }
            }
            if (classifiedCategory != null &&
                    !"No Match".equalsIgnoreCase(classifiedCategory) &&
                    categories.contains(classifiedCategory)) {
                categorizedFeedbacks.get(classifiedCategory).add(feedback);
            }
        }
        return categorizedFeedbacks;
    }

    /**
     * Creates or updates category summary for a batch of feedbacks belonging to the category.
     */
    private void updateOrCreateSummary(Product product, Category category, CategorySummary existingSummary,
                                       List<FeedbackDTO> categoryFeedbacks) {
        if (existingSummary != null) {
            if (!categoryFeedbacks.isEmpty()) {
                String updatedSummary = aiUtils.generateIncrementalSummary(
                        category.getName(), categoryFeedbacks, existingSummary.getSummaryText());
                existingSummary.setSummaryText(updatedSummary);
                categorySummaryRepository.save(existingSummary);
            }
        } else {
            String summary = categoryFeedbacks.isEmpty()
                    ? "No specific feedback available for this category."
                    : generateSummaryForCategorizedFeedbacks(category.getName(), categoryFeedbacks);
            CategorySummary categorySummary = new CategorySummary();
            categorySummary.setProduct(product);
            categorySummary.setCategory(category);
            categorySummary.setSummaryText(summary);
            categorySummaryRepository.save(categorySummary);
        }
    }

    /**
     * Orchestrates category assignment and AI summary generation for a new or regenerated category set.
     */
    private void generateAndSaveSummaries(Product product, List<Category> categories,
                                          List<FeedbackDTO> feedbacks) {
        Map<String, Category> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getName, c -> c));
        Map<String, List<FeedbackDTO>> categorizedFeedbacks =
                classifyFeedbacksIntoCategories(categories.stream().map(Category::getName).toList(),
                        feedbacks, categoryMap);

        for (Category category : categories) {
            List<FeedbackDTO> categoryFeedbacks = categorizedFeedbacks.getOrDefault(
                    category.getName(), Collections.emptyList());
            String summary = categoryFeedbacks.isEmpty()
                    ? "No specific feedback available for this category."
                    : generateSummaryForCategorizedFeedbacks(category.getName(), categoryFeedbacks);
            CategorySummary categorySummary = new CategorySummary();
            categorySummary.setProduct(product);
            categorySummary.setCategory(category);
            categorySummary.setSummaryText(summary);
            categorySummaryRepository.save(categorySummary);
        }
    }

    /**
     * Generates a concise summary using AI for a batch of feedbacks (falling back gracefully).
     */
    private String generateSummaryForCategorizedFeedbacks(String categoryName, List<FeedbackDTO> feedbacks) {
        if (feedbacks.isEmpty()) {
            return "No specific feedback available for this category.";
        }
        StringBuilder prompt = new StringBuilder();
        prompt.append("Summarize the following user feedbacks for the category: ")
                .append(categoryName).append(".\nNumber of feedbacks: ").append(feedbacks.size()).append("\nFeedbacks:\n");
        for (FeedbackDTO feedback : feedbacks) {
            prompt.append("- ").append(feedback.getMessage()).append("\n");
        }
        prompt.append("\nProvide a concise 2-3 line summary that captures:\n")
                .append("1. Main themes and common issues/praise\n")
                .append("2. Overall sentiment (positive/negative/mixed)\n")
                .append("3. Key actionable insights");
        try {
            return aiUtils.generateSummary(prompt.toString());
        } catch (Exception e) {
            log.error("Error generating summary for category '{}': {}", categoryName, e.getMessage(), e);
            return "Summary generation failed due to technical error.";
        }
    }
}
