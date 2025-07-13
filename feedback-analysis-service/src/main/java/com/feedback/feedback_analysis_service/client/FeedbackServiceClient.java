package com.feedback.feedback_analysis_service.client;

import com.feedback.feedback_analysis_service.dto.FeedbackDTO;
import com.feedback.feedback_analysis_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "feedback-service", url = "http://localhost:9099/api")
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
}
