package com.feedback.feedback_service.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiSentimentService {

    private final Client geminiClient;

    public GeminiSentimentService(@Value("$(GOOGLE_API_KEY)") String apiKey) {

        // Automatically picks up GOOGLE_API_KEY from environment
        this.geminiClient = Client.builder()
                .apiKey("AIzaSyCGIDNDmAGNA9OweEohN5RU5U2utt8eYLo")
                .build();
    }

    public String analyzeSentiment(String feedbackText) {

        String prompt = "Classify the following feedback as Positive, Negative, or Neutral:\n\n" + feedbackText;

        GenerateContentResponse response = geminiClient.models.generateContent(
                "gemini-2.5-flash",  // or gemini-2.5-flash if supported
                prompt,
                null
        );
        System.out.println(response.text());
        String fullText = response.text().toLowerCase(); // Normalize casing

        if (fullText.contains("positive")) {
            return "Positive";
        } else if (fullText.contains("negative")) {
            return "Negative";
        } else {
            return "Neutral";
        }
    }
}
