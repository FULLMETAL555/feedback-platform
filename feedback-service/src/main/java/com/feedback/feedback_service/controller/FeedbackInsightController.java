package com.feedback.feedback_service.controller;


import com.feedback.feedback_service.dto.FeedbackSentimentSummary;
import com.feedback.feedback_service.dto.FeedbackStatResponseDTO;
import com.feedback.feedback_service.dto.ProductFeedbackSummaryDTO;
import com.feedback.feedback_service.model.Feedback;
import com.feedback.feedback_service.service.FeedbackService;
import com.feedback.feedback_service.service.FeedbackStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/insights")
public class FeedbackInsightController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackStatsService feedbackStatsService;

    @GetMapping("/feedbacks")
    public List<Feedback> getFeedbacks(@RequestParam Long productId, @RequestHeader("X-API-KEY") String apiKey){
        return feedbackService.getFeedbacksByProductAndApiKey(productId,apiKey);
    }

    @GetMapping("/getfeedbysen")
    public List<Feedback> getFeedbackBySentiment(@RequestParam String sentiment, @RequestParam Long productId,@RequestHeader("X-API-KEY") String apiKey, @PageableDefault(size = 10,sort = "submittedAt",direction = Sort.Direction.DESC) Pageable pageable){
        return feedbackService.getFilteredFeedbacks(sentiment,productId,pageable,apiKey);
    }

    @GetMapping("/summary")
    public List<FeedbackSentimentSummary> getFeedbackSummary(@RequestParam Long productId,@RequestHeader("X-API-KEY") String apiKey) {
        return feedbackService.getFeedbackSummaryByProduct(productId,apiKey);
    }

    @GetMapping("/dashboard")
    public List<ProductFeedbackSummaryDTO> getClientDashboard(@RequestHeader("X-API-KEY") String apiKey) {
        return feedbackService.getProductSummaries(apiKey);
    }
    @GetMapping("/api/feedback/stats/recent")
    public ResponseEntity<FeedbackStatResponseDTO> getRecentStats(@RequestHeader("X-API-KEY") String apiKey) {
        return ResponseEntity.ok(feedbackStatsService.getRecentFeedbackStats(apiKey));
    }
}
