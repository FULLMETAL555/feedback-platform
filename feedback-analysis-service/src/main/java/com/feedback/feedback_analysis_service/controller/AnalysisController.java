package com.feedback.feedback_analysis_service.controller;

import com.feedback.feedback_analysis_service.service.FeedbackProcessingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis")
@AllArgsConstructor
public class AnalysisController {

    private final FeedbackProcessingService feedbackProcessingService;

//    @GetMapping("/{productId}")
//    public ResponseEntity<String> analyzeFeedbacks(@PathVariable Long productId){
//            feedbackProcessingService.processFeedbacksForProduct(productId);
//
//            return  ResponseEntity.ok("Analysis completed for product ID :"+ productId);
//

    @PostMapping("/generate/{productId}")
    public ResponseEntity<String> generateCategoriesAndSummaries(@PathVariable Long productId) {
        feedbackProcessingService.processFeedbackForProduct(productId);
        return ResponseEntity.ok("Category generation and summary complete.");
    }
}
