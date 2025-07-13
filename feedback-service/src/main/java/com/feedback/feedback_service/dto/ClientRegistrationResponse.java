package com.feedback.feedback_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientRegistrationResponse {
    private String apiKey;
    private String clientId;
    private String email;
    private String name;
}