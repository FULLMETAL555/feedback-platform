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

    @Transactional
    public void processFeedbackForProduct(Long productId) {
        // 1. Fetch product
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            log.warn("Product with ID {} not found.", productId);
            return;
        }
        Product product = productOpt.get();

        // 2. Fetch feedbacks using Feign
        List<FeedbackDTO> feedbackList = feedbackServiceClient.getFeedbacksForProduct(productId);
        if (feedbackList.size() < 3) {
            log.info("Not enough feedbacks to process (need at least 3). Found: {}", feedbackList.size());
            return;
        }

        // 3. Fetch existing categories (if any)
        List<Category> existingCategories = categoryRepository.findByProductId(productId);
        if (!existingCategories.isEmpty()) {
            log.info("Categories already exist for product {}. Skipping generation.", productId);
            return;
        }

        // 4. Generate categories using AI
        List<String> feedbackTexts = feedbackList.stream()
                .map(FeedbackDTO::getMessage)
                .toList();

        String context = product.getName() + " - " + product.getDescription();
        List<String> categories = categoryGenerationUtil.generateCategories(product.getName(),product.getDescription(),feedbackTexts);

        // 5. Save categories
        List<Category> savedCategories = categories.stream()
                .map(cat -> {
                    Category c = new Category();
                    c.setProduct(product);
                    c.setName(cat);
                    return c;
                })
                .map(categoryRepository::save)
                .collect(Collectors.toList());

        log.info("Saved categories: {}", savedCategories.stream().map(Category::getName).toList());

        // 6. Summarize feedbacks per category
        for (Category category : savedCategories) {
            String summary = aiUtils.summerizeFeedbackForCategory(category.getName(), feedbackList);

            CategorySummary categorySummary = new CategorySummary();
            categorySummary.setProduct(product);
            categorySummary.setCategory(category);
            categorySummary.setSummaryText(summary);

            categorySummaryRepository.save(categorySummary);
        }

        log.info("Saved summaries for {} categories", savedCategories.size());
    }
}
