package com.feedback.feedback_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnonymizationResponse {
    private String anonymizedMessage;
}
