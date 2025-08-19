package com.feedback.feedback_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedbackTrendsDTO {

    private String period;
    private long feedbackCount;
    private double avgRating;


}
