package com.feedback.feedback_service.service;

import com.feedback.feedback_service.client.AnonymizationFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnonymizationClient {

    private final AnonymizationFeignClient anonymizationFeignClient;

    @Autowired
    public AnonymizationClient(AnonymizationFeignClient anonymizationFeignClient) {
        this.anonymizationFeignClient = anonymizationFeignClient;
    }

    public String anonymize(String message) {
        try {
            System.out.println("MESSAGE: " + message);

            Map<String, String> request = new HashMap<>();
            request.put("message", message);

            return anonymizationFeignClient.anonymize(request);

        } catch (Exception e) {
            System.err.println("Error during anonymization: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
