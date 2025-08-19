package com.feedback.feedback_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInsightResponseDTO {

    private String productId;
    private String productName;
    private double averageRating;
    private int totalFeedbackCount;
    private int positivePercentage;
    private int neutralPercentage;
    private int negativePercentage;
    private List<SentimentCount> topSentiments;
    private List<ThemeSentiment> commonThemes;
    private String latestFeedbackDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SentimentCount {
        private String sentiment;
        private int count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThemeSentiment {
        private String theme;
        private int mentionCount;
        private SentimentRatio sentimentRatio;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SentimentRatio {
        private double positive;
        private double neutral;
        private double negative;
    }
}