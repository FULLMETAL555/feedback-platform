package com.feedback.feedback_service.dto;

import lombok.Data;

@Data
public class TrendPointDTO {
    private String period; // e.g., "2025-08"
    private long totalCount;
    private long positiveCount;
    private long negativeCount;
    private long neutralCount;
}
