package com.feedback.feedback_service.dto;

import lombok.Data;

@Data
public class ProductPerformanceDTO {
    private String productName;
    private long totalFeedback;
    private double positivePercentage;
    private String lastFeedbackDate;
}
