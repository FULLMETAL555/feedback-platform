package com.feedback.feedback_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnonymizationClient {

    @Value("${anonymization.service.url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate; // Inject instead of creating new instance

    public String anonymize(String message) {
        try {
            System.out.println("MESSAGE: " + message);

            // Prepare request body
            Map<String, String> request = new HashMap<>();
            request.put("message", message);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

            // Send request and get response
            return restTemplate.postForObject(this.url, entity, String.class);

        } catch (ResourceAccessException e) {
            // Handle connection refused or service down
            System.err.println("Anonymization service unavailable: " + e.getMessage());
            throw new RuntimeException(e);
           // return message; // Return original message if service is down
        } catch (Exception e) {
            // Handle other errors
            System.err.println("Error during anonymization: " + e.getMessage());
           // return message; // Return original message on error
            throw new RuntimeException(e);

        }
    }
}