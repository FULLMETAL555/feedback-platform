package com.feedback.feedback_analysis_service.dto;

import lombok.Data;

@Data
public class FeedbackSummaryDTO {
    private Long productId;
    private String category;
    private String summary;
}
