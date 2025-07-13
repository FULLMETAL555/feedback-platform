package com.feedback.feedback_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedbackStatResponseDTO {

    private long total;
    private long positive;
    private long negative;
    private long neutral;

}
