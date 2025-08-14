package com.feedback.feedback_service.dto;

import lombok.Data;

@Data
public class SentimentDistributionDTO {
    private String sentiment; // Positive, Negative, Neutral
    private long count;
}
