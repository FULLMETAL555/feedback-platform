package com.feedback.feedback_service.dto;

import lombok.Data;

@Data
public class RecentFeedbackDTO {
    private String date;
    private String message;
    private String sentiment;
    private String productName;
}
