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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackProcessingService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategorySummaryRepository categorySummaryRepository;
    private final CategoryGenerationUtil categoryUtil;
    private final AIUtils aiUtils;
    private final FeedbackServiceClient feedbackClient; // Still used for feedbacks

    public void processFeedbacksForProduct(Long productId) {
        log.info("🔍 Starting feedback analysis for product ID: {}", productId);

        List<FeedbackDTO> feedbacks = feedbackClient.getFeedbacksForProduct(productId);
        if (feedbacks.size() < 4) {
            log.info("⚠️ Not enough feedback to process product ID: {}", productId);
            return;
        }

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            log.warn("❌ Product not found with ID: {}", productId);
            return;
        }
        Product product = optionalProduct.get();

        // Step 1: Check if categories already exist
        List<Category> existingCategories = categoryRepository.findByProductId(productId);
        List<String> categoryNames;

        if (existingCategories.isEmpty()) {
            log.info("📦 No categories found. Generating new categories.");
            categoryNames = generateCategories(feedbacks, product);
            saveCategories(product, categoryNames);
        } else {
            categoryNames = existingCategories.stream().map(Category::getName).collect(Collectors.toList());
            log.info("✅ Categories already exist: {}", categoryNames);
        }

        // Step 2: Classify feedbacks into categories
        Map<String, List<FeedbackDTO>> feedbackByCategory = new HashMap<>();
        for (FeedbackDTO feedback : feedbacks) {
            String category = aiUtils.classifyFeedbackIntoCategory(categoryNames, feedback.getMessage());
            if (!"No Match".equalsIgnoreCase(category)) {
                feedbackByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(feedback);
            } else {
                log.info("⚠️ Feedback not matched to any category: {}", feedback.getMessage());
            }
        }

        // Step 3: Generate summary per category
        List<CategorySummary> summaries = new ArrayList<>();
        for (Map.Entry<String, List<FeedbackDTO>> entry : feedbackByCategory.entrySet()) {
            String category = entry.getKey();
            List<FeedbackDTO> feedbackList = entry.getValue();
            String summary = aiUtils.summerizeFeedbackForCategory(category, feedbackList);

            CategorySummary categorySummary = new CategorySummary();
            categorySummary.setProduct(product);
            categorySummary.setCategoryName(category);
            categorySummary.setSummary(summary);
            summaries.add(categorySummary);
        }

        categorySummaryRepository.saveAll(summaries);
        log.info("✅ Saved {} summaries for product {}", summaries.size(), productId);
    }

    private List<String> generateCategories(List<FeedbackDTO> feedbacks, Product product) {
        List<String> feedbackTexts = feedbacks.stream()
                .map(FeedbackDTO::getMessage)
                .collect(Collectors.toList());

        return categoryUtil.generateCategories(
                product.getName(),
                product.getDescription(),
                feedbackTexts
        );
    }

    private void saveCategories(Product product, List<String> categoryNames) {
        List<Category> categories = categoryNames.stream()
                .map(name -> {
                    Category c = new Category();
                    c.setName(name);
                    c.setProduct(product);
                    return c;
                }).collect(Collectors.toList());

        categoryRepository.saveAll(categories);
        log.info("✅ Saved {} new categories for product ID {}", categories.size(), product.getId());
    }
}
