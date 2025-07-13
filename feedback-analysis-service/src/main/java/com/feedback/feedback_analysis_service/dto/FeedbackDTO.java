package com.feedback.feedback_analysis_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackDTO {
    private Long id;
    private String message;
    private String sentiment;
    private String submittedBy;
    private LocalDateTime submittedAt;
}
