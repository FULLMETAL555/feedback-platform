package com.feedback.feedback_analysis_service.dto;

import lombok.Data;

@Data
public class CategorySummaryDTO {
    private Long productId;
    private String category;
    private String summary;
}
