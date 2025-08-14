package com.feedback.feedback_analysis_service.client;

import com.feedback.feedback_analysis_service.config.FeignClientConfig;
import com.feedback.feedback_analysis_service.dto.CategorySummaryDTO;
import com.feedback.feedback_analysis_service.dto.FeedbackDTO;
import com.feedback.feedback_analysis_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "feedback-service", url = "${feedback.service.url}",configuration = FeignClientConfig.class)
public interface FeedbackServiceClient {

    @GetMapping("/feedback/product/{productId}")
    List<FeedbackDTO> getFeedbacksForProduct(@PathVariable("productId") Long productId);

    @GetMapping("/products/{productId}")
    ProductDTO getProductDetails(@PathVariable("productId") Long productId);

    @PostMapping("/feedback/categories/{productId}")
    void saveCategorySummary(
            @PathVariable("productId") Long productId,
            @RequestBody List<String> categories
    );

    @PostMapping("/feedback/summary/{productId}")
    void saveFeedbackSummary(
            @PathVariable("productId") Long productId,
            @RequestBody List<String> summaries
    );

    // Add HTTP method annotation for this method
    @PostMapping("/feedback/category-summary")
    void saveCategorySummary(@RequestBody CategorySummaryDTO categorySummaryDTO);

    // Add HTTP method annotation for this method
    @GetMapping("/feedback/categories/{productId}")
    List<String> getExistingCategories(@PathVariable("productId") Long productId);

    @PutMapping("/feedback/{id}/category")
    void updateFeedbackCategory(@PathVariable("id") Long feedbackId, @RequestParam("categoryId") Long categoryId);

}