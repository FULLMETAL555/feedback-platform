package com.feedback.feedback_analysis_service.service;

import com.feedback.feedback_analysis_service.client.FeedbackServiceClient;
import com.feedback.feedback_analysis_service.dto.FeedbackDTO;
import com.feedback.feedback_analysis_service.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackProcessingService {

    private final FeedbackServiceClient feedbackClient;

    public void processFeedbacksForProduct(Long productId) {
        List<FeedbackDTO> feedbacks = feedbackClient.getFeedbacksForProduct(productId);

        if (feedbacks.size() < 3) {
            System.out.println("Not enough feedback to generate categories/summaries");
            return;
        }

        ProductDTO product = feedbackClient.getProductDetails(productId);

        // Step 1: Generate 4 AI categories based on feedbacks
        List<String> categories = generateCategories(feedbacks, product);

        // Step 2: Group feedbacks by category (for now assign randomly for mock)
        Map<String, List<FeedbackDTO>> feedbackByCategory = assignFeedbackToCategories(categories, feedbacks);

        // Step 3: Generate summaries for each category
        List<String> summaries = feedbackByCategory.entrySet().stream()
                .map(entry -> summarizeFeedbacks(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // Step 4: Save to monolithic service
        feedbackClient.saveCategorySummary(productId, categories);
        feedbackClient.saveFeedbackSummary(productId, summaries);
    }

    // MOCK: Replace with AI logic later
    private List<String> generateCategories(List<FeedbackDTO> feedbacks, ProductDTO product) {
        return List.of("Pricing", "Usability", "Performance", "Customer Support");
    }

    // MOCK: Randomly assign for now
    private Map<String, List<FeedbackDTO>> assignFeedbackToCategories(List<String> categories, List<FeedbackDTO> feedbacks) {
        Map<String, List<FeedbackDTO>> map = new HashMap<>();
        Random rand = new Random();

        for (FeedbackDTO feedback : feedbacks) {
            String category = categories.get(rand.nextInt(categories.size()));
            map.computeIfAbsent(category, k -> new ArrayList<>()).add(feedback);
        }

        return map;
    }

    // MOCK: Simulate AI summary
    private String summarizeFeedbacks(String category, List<FeedbackDTO> feedbacks) {
        return "Summary for " + category + ": " + feedbacks.size() + " feedback(s) analyzed.";
    }
}
