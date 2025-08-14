package com.feedback.feedback_service.dto;

import lombok.Data;

@Data
public class KpiStatsDTO {
    private long totalFeedbacks;
    private long numberOfProducts;
    private double positivePercentage;
}
