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

    private static final double UNCLASSIFIED_THRESHOLD = 0.10; // 10% unclassified

    @Transactional
    public void processFeedbackForProduct(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) return;
        Product product = productOpt.get();

        List<FeedbackDTO> allFeedbacks = feedbackServiceClient.getFeedbacksForProduct(productId);
        if (allFeedbacks.size() < 4) return;

        List<Category> existingCategories = categoryRepository.findByProductId(productId);

        if (existingCategories.isEmpty()) {
            processInitialFeedbacks(product, allFeedbacks);
        } else {
            // Hybrid/adaptive: check how many feedbacks cannot be classified with existing categories
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

    private boolean isCategoryCoverageSufficient(List<String> existingCategoryNames, List<FeedbackDTO> feedbacks, double thresholdPercent) {
        long unclassifiedCount = feedbacks.stream()
                .filter(feedback -> {
                    String classified = aiUtils.classifyFeedbackIntoCategory(existingCategoryNames, feedback.getMessage());
                    return "No Match".equalsIgnoreCase(classified.trim());
                })
                .count();

        double unclassifiedRatio = (double) unclassifiedCount / feedbacks.size();
        log.info("Unclassified feedbacks: {}/{} = {}%", unclassifiedCount, feedbacks.size(), unclassifiedRatio * 100);
        return unclassifiedRatio <= thresholdPercent;
    }

    // Called only if not enough categories or coverage is poor
    private void processCategoryRegeneration(Product product, List<FeedbackDTO> allFeedbacks) {
        categorySummaryRepository.deleteAllByProductId(product.getId());
        categoryRepository.deleteByProductId(product.getId());
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
                })
                .toList();
        generateAndSaveSummaries(product, newCategories, allFeedbacks);
    }

    // Called only on very first batch, or after regeneration
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
        generateAndSaveSummaries(product, savedCategories, feedbacks);
    }

    // Standard incremental summary updates for existing category set
    private void processIncrementalFeedbacks(Product product, List<Category> existingCategories,
                                             List<FeedbackDTO> allFeedbacks) {
        List<CategorySummary> existingSummaries = categorySummaryRepository.findByProductId(product.getId());
        Map<Long, CategorySummary> summaryByCategoryId = existingSummaries.stream()
                .collect(Collectors.toMap(
                        summary -> summary.getCategory().getId(),
                        summary -> summary
                ));

        for (Category category : existingCategories) {
            CategorySummary existingSummary = summaryByCategoryId.get(category.getId());
            updateOrCreateSummary(product, category, existingSummary, allFeedbacks);
        }
    }

    // Creates or updates a summary for a given category
    private void updateOrCreateSummary(Product product, Category category, CategorySummary existingSummary,
                                       List<FeedbackDTO> allFeedbacks) {
        List<FeedbackDTO> categoryFeedbacks = classifyFeedbacksForCategory(category.getName(), allFeedbacks);
        if (existingSummary != null) {
            if (!categoryFeedbacks.isEmpty()) {
                String updatedSummary = aiUtils.generateIncrementalSummary(
                        category.getName(),
                        categoryFeedbacks,
                        existingSummary.getSummaryText()
                );
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

    // Main summary generation for a batch of categories
    private void generateAndSaveSummaries(Product product, List<Category> categories,
                                          List<FeedbackDTO> feedbacks) {
        List<String> categoryNames = categories.stream().map(Category::getName).toList();
        Map<String, List<FeedbackDTO>> categorizedFeedbacks = classifyFeedbacksIntoCategories(categoryNames, feedbacks);

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

    private List<FeedbackDTO> classifyFeedbacksForCategory(String categoryName, List<FeedbackDTO> feedbacks) {
        return feedbacks.stream()
                .filter(feedback -> categoryName.equalsIgnoreCase(
                        aiUtils.classifyFeedbackIntoCategory(List.of(categoryName), feedback.getMessage()).trim()))
                .collect(Collectors.toList());
    }

    private Map<String, List<FeedbackDTO>> classifyFeedbacksIntoCategories(
            List<String> categories, List<FeedbackDTO> feedbacks) {
        Map<String, List<FeedbackDTO>> categorizedFeedbacks = new HashMap<>();
        categories.forEach(category -> categorizedFeedbacks.put(category, new ArrayList<>()));

        for (FeedbackDTO feedback : feedbacks) {
            String classifiedCategory = aiUtils.classifyFeedbackIntoCategory(categories, feedback.getMessage()).trim();
            if (!"No Match".equalsIgnoreCase(classifiedCategory) &&
                    categories.contains(classifiedCategory)) {
                categorizedFeedbacks.get(classifiedCategory).add(feedback);
            }
        }
        return categorizedFeedbacks;
    }

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
