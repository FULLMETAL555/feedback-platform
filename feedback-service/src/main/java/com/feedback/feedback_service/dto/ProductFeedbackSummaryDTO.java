package com.feedback.feedback_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProductFeedbackSummaryDTO {
    private Long productId;
    private String productName;
    private long totalFeedbacks;
    private LocalDateTime lastFeedbackTime;
}
