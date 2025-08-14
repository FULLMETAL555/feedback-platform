package com.feedback.feedback_service.dto;

import lombok.Data;

@Data
public class CategoryInsightDTO {
    private String categoryName;
    private String summary;
    private long feedbackCount;
    private double positivePercentage;
}
