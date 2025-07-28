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

import java.sql.Timestamp;
import java.time.Instant;
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

    @Transactional
    public void processFeedbackForProduct(Long productId) {
        // 1. Fetch product
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            log.warn("Product with ID {} not found.", productId);
            return;
        }
        Product product = productOpt.get();

        // 2. Fetch all feedbacks using Feign
        List<FeedbackDTO> allFeedbacks = feedbackServiceClient.getFeedbacksForProduct(productId);
        if (allFeedbacks.size() < 4) {
            log.info("Not enough feedbacks to process (need at least 4). Found: {}", allFeedbacks.size());
            return;
        }

        // 3. Check if categories already exist
        List<Category> existingCategories = categoryRepository.findByProductId(productId);

        if (existingCategories.isEmpty()) {
            // First time processing - generate categories and summaries
            log.info("No existing categories found. Processing all feedbacks for the first time.");
            processInitialFeedbacks(product, allFeedbacks);
        } else {
            // Categories exist - process incremental feedbacks
            log.info("Found {} existing categories. Processing incremental feedbacks.", existingCategories.size());
            processIncrementalFeedbacks(product, existingCategories, allFeedbacks);
        }
    }

    /**
     * Process feedbacks for the first time - generate categories and initial summaries
     */
    private void processInitialFeedbacks(Product product, List<FeedbackDTO> feedbacks) {
        // Extract feedback texts
        List<String> feedbackTexts = feedbacks.stream()
                .map(FeedbackDTO::getMessage)
                .toList();

        // Generate categories
        List<String> generatedCategories = categoryGenerationUtil.generateCategories(
                product.getName(),
                product.getDescription(),
                feedbackTexts
        );

        log.info("Generated categories: {}", generatedCategories);

        // Save categories
        List<Category> savedCategories = generatedCategories.stream()
                .map(categoryName -> {
                    Category category = new Category();
                    category.setProduct(product);
                    category.setName(categoryName);
                    return categoryRepository.save(category);
                })
                .collect(Collectors.toList());

        log.info("Saved initial categories: {}", savedCategories.stream().map(Category::getName).toList());

        // Generate initial summaries
        generateAndSaveSummaries(product, savedCategories, feedbacks, false);
    }

    /**
     * Process incremental feedbacks - update existing summaries with new feedbacks
     */
    private void processIncrementalFeedbacks(Product product, List<Category> existingCategories,
                                             List<FeedbackDTO> allFeedbacks) {

        // Get existing summaries
        List<CategorySummary> existingSummaries = categorySummaryRepository.findByProductId(product.getId());
        Map<Long, CategorySummary> summaryByCategoryId = existingSummaries.stream()
                .collect(Collectors.toMap(
                        summary -> summary.getCategory().getId(),
                        summary -> summary
                ));

        log.info("Found {} existing summaries for incremental processing", existingSummaries.size());

        // Update summaries with all feedbacks (including new ones)
        for (Category category : existingCategories) {
            CategorySummary existingSummary = summaryByCategoryId.get(category.getId());

            if (existingSummary != null) {
                // Update existing summary
                updateExistingSummary(category, existingSummary, allFeedbacks);
            } else {
                // Create new summary if somehow missing
                log.warn("No existing summary found for category: {}. Creating new one.", category.getName());
                createNewSummaryForCategory(product, category, allFeedbacks);
            }
        }
    }

    /**
     * Update existing summary with all feedbacks (old + new)
     */
    private void updateExistingSummary(Category category, CategorySummary existingSummary,
                                       List<FeedbackDTO> allFeedbacks) {

        // Classify all feedbacks for this category
        List<FeedbackDTO> categoryFeedbacks = classifyFeedbacksForCategory(
                category.getName(), allFeedbacks);

        if (categoryFeedbacks.isEmpty()) {
            log.info("No feedbacks classified for category '{}'. Keeping existing summary.", category.getName());
            return;
        }

        String updatedSummary = aiUtils.generateIncrementalSummary(
                category.getName(),
                categoryFeedbacks,
                existingSummary.getSummaryText()
        );

        existingSummary.setSummaryText(updatedSummary);

        categorySummaryRepository.save(existingSummary);

        log.info("Updated summary for category '{}' using {} feedbacks",
                category.getName(), categoryFeedbacks.size());
    }

    /**
     * Create new summary for a category (fallback case)
     */
    private void createNewSummaryForCategory(Product product, Category category, List<FeedbackDTO> allFeedbacks) {
        List<FeedbackDTO> categoryFeedbacks = classifyFeedbacksForCategory(
                category.getName(), allFeedbacks);

        String summary = categoryFeedbacks.isEmpty()
                ? "No specific feedback available for this category."
                : generateSummaryForCategorizedFeedbacks(category.getName(), categoryFeedbacks);

        CategorySummary categorySummary = new CategorySummary();
        categorySummary.setProduct(product);
        categorySummary.setCategory(category);
        categorySummary.setSummaryText(summary);


        categorySummaryRepository.save(categorySummary);
    }

    /**
     * Generate and save summaries for multiple categories
     */
    private void generateAndSaveSummaries(Product product, List<Category> categories,
                                          List<FeedbackDTO> feedbacks, boolean isUpdate) {

        // Get all category names for classification
        List<String> categoryNames = categories.stream()
                .map(Category::getName)
                .toList();

        // Classify feedbacks into categories
        Map<String, List<FeedbackDTO>> categorizedFeedbacks = classifyFeedbacksIntoCategories(
                categoryNames, feedbacks);

        // Generate summaries for each category
        for (Category category : categories) {
            List<FeedbackDTO> categoryFeedbacks = categorizedFeedbacks.getOrDefault(
                    category.getName(),
                    Collections.emptyList()
            );

            String summary;
            if (categoryFeedbacks.isEmpty()) {
                summary = "No specific feedback available for this category.";
                log.warn("No feedbacks classified for category: {}", category.getName());
            } else {
                summary = generateSummaryForCategorizedFeedbacks(category.getName(), categoryFeedbacks);
                log.info("Generated summary for category '{}' using {} feedbacks",
                        category.getName(), categoryFeedbacks.size());
            }

            CategorySummary categorySummary = new CategorySummary();
            categorySummary.setProduct(product);
            categorySummary.setCategory(category);
            categorySummary.setSummaryText(summary);


            categorySummaryRepository.save(categorySummary);
        }

        log.info("Successfully processed and saved summaries for {} categories", categories.size());
    }

    /**
     * Classify feedbacks for a specific category
     */
    private List<FeedbackDTO> classifyFeedbacksForCategory(String categoryName, List<FeedbackDTO> feedbacks) {
        return feedbacks.stream()
                .filter(feedback -> {
                    String classified = aiUtils.classifyFeedbackIntoCategory(
                            List.of(categoryName),
                            feedback.getMessage()
                    );
                    return categoryName.equalsIgnoreCase(classified.trim()) ||
                            !classified.trim().equalsIgnoreCase("No Match");
                })
                .collect(Collectors.toList());
    }

    /**
     * Classify each feedback into the most appropriate category
     */
    private Map<String, List<FeedbackDTO>> classifyFeedbacksIntoCategories(
            List<String> categories, List<FeedbackDTO> feedbacks) {

        Map<String, List<FeedbackDTO>> categorizedFeedbacks = new HashMap<>();

        // Initialize empty lists for each category
        categories.forEach(category -> categorizedFeedbacks.put(category, new ArrayList<>()));

        // Classify each feedback
        for (FeedbackDTO feedback : feedbacks) {
            String classifiedCategory = aiUtils.classifyFeedbackIntoCategory(categories, feedback.getMessage());

            if (!"No Match".equalsIgnoreCase(classifiedCategory.trim()) &&
                    categories.contains(classifiedCategory.trim())) {
                categorizedFeedbacks.get(classifiedCategory.trim()).add(feedback);
            } else {
                // If no match, try to find best fit or assign to a default category
                String bestFitCategory = findBestFitCategory(categories, feedback.getMessage());
                categorizedFeedbacks.get(bestFitCategory).add(feedback);
            }
        }

        // Log classification results
        categorizedFeedbacks.forEach((category, feedbacksInCategory) -> {
            log.info("Category '{}' has {} classified feedbacks", category, feedbacksInCategory.size());
        });

        return categorizedFeedbacks;
    }

    /**
     * Find the best fitting category for unmatched feedback
     */
    private String findBestFitCategory(List<String> categories, String feedbackText) {
        // Simple keyword-based fallback logic
        String feedback = feedbackText.toLowerCase();

        for (String category : categories) {
            String categoryLower = category.toLowerCase();
            if (feedback.contains("performance") || feedback.contains("speed") || feedback.contains("slow")) {
                if (categoryLower.contains("performance")) return category;
            }
            if (feedback.contains("ui") || feedback.contains("design") || feedback.contains("interface")) {
                if (categoryLower.contains("ui") || categoryLower.contains("design")) return category;
            }
            if (feedback.contains("feature") || feedback.contains("function")) {
                if (categoryLower.contains("feature") || categoryLower.contains("function")) return category;
            }
            if (feedback.contains("battery") || feedback.contains("power")) {
                if (categoryLower.contains("battery") || categoryLower.contains("power")) return category;
            }
            if (feedback.contains("comfort") || feedback.contains("ergonomic")) {
                if (categoryLower.contains("comfort") || categoryLower.contains("ergonomic")) return category;
            }
        }

        // Default to first category if no match found
        return categories.get(0);
    }

    /**
     * Generate summary for properly categorized feedbacks
     */
    private String generateSummaryForCategorizedFeedbacks(String categoryName, List<FeedbackDTO> feedbacks) {
        if (feedbacks.isEmpty()) {
            return "No specific feedback available for this category.";
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("Summarize the following user feedbacks for the category: ")
                .append(categoryName).append(".\n")
                .append("Number of feedbacks: ").append(feedbacks.size()).append("\n")
                .append("Feedbacks:\n");

        for (FeedbackDTO feedback : feedbacks) {
            prompt.append("- ").append(feedback.getMessage()).append("\n");
        }

        prompt.append("\nProvide a concise 2-3 line summary that captures:")
                .append("\n1. Main themes and common issues/praise")
                .append("\n2. Overall sentiment (positive/negative/mixed)")
                .append("\n3. Key actionable insights");

        try {
            return aiUtils.generateSummary(prompt.toString());
        } catch (Exception e) {
            log.error("Error generating summary for category '{}': {}", categoryName, e.getMessage(), e);
            return "Summary generation failed due to technical error.";
        }
    }
}
