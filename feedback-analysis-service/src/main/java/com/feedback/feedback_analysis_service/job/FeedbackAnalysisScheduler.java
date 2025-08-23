package com.feedback.feedback_analysis_service.job;

import com.feedback.feedback_analysis_service.model.Product;
import com.feedback.feedback_analysis_service.repository.ProductRepository;
import com.feedback.feedback_analysis_service.service.FeedbackProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
@Component // Add this!
@Slf4j
public class FeedbackAnalysisScheduler {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FeedbackProcessingService feedbackProcessingService;

    // Run every hour
    @Scheduled(cron = "0 0 * * * *")
    public void runAnalysisForNewFeedbackProducts() {
        log.info("Starting scheduled feedback analysis");
        List<Product> products = productRepository.findProductsWithUncategorizedFeedback();
        log.info("Found {} products without categories", products.size());

        for (Product product : products) {
            try {
                feedbackProcessingService.processFeedbackForProduct(product.getId());
                log.debug("Processed feedback for product: {}", product.getId());
            } catch (Exception e) {
                log.error("Failed to process feedback for product: {}", product.getId(), e);
            }
        }
        log.info("Completed scheduled feedback analysis");
    }

    // Add manual trigger for testing
    public void runAnalysisManually() {
        runAnalysisForNewFeedbackProducts();
    }
}
