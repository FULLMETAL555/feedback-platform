package com.feedback.feedback_service.dto;

public record SignUpDTO(
        String name,
        String email,
        String password
) {}