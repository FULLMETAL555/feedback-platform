package com.anonymizer.data_anonymization_service.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AnonymizeService {
    public String anonymizeFeedback(Map<String, String> request) {
        String message = request.get("message");
        // Basic masking examples

        // Replace emails
        message = message.replaceAll("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", "anonymous@example.com");

        // Replace phone numbers (very simple pattern)
        message = message.replaceAll("\\b\\d{10}\\b", "XXXXXXXXXX");

        return message;
    }
}
