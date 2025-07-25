package com.feedback.feedback_analysis_service.util;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CategoryGenerationUtil {

    private final Client geminiClient;

    public CategoryGenerationUtil(@Value("$(GOOGLE_API_KEY)") String apiKey){
        this.geminiClient = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public List<String> generateCategories(String productName, String productDescription, List<String> feedbacks){
        StringBuilder prompt =new StringBuilder();
        prompt.append("Product: ")
                .append(productName)
                .append("\n")
                .append("Description: ").append(productDescription).append("\n")
                .append("Feedbacks:\n");

        for (String s:feedbacks){
            prompt.append("- ")
                    .append(s)
                    .append("\n");
        }
        prompt.append("\nGenerate 4 high-level feedback categories based on the feedback and product.");


        try {
            GenerateContentResponse response=geminiClient.models.generateContent(
                    "gemini-2.5-flash",  // or gemini-2.5-flash if supported
                    prompt.toString(),
                    null
            );

            String text = response.text();
            return text.lines()
                    .map(String::trim)
                    .filter(line->!line.isEmpty())
                    .map(line -> line.replaceAll("^[0-9\\.\\-\\)\\s]+", ""))
                    .toList();



        }catch (Exception e) {
            log.error("Error generating categories: {}", e.getMessage(), e);
            return List.of("General", "Features", "UI/UX", "Performance");
        }
    }


}
