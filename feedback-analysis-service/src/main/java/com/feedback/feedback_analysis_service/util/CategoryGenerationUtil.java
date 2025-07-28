package com.feedback.feedback_analysis_service.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CategoryGenerationUtil {

    private final Client geminiClient;

    @Autowired
    private ObjectMapper objectMapper;

    public CategoryGenerationUtil(@Value("$(GOOGLE_API_KEY)") String apiKey){
        this.geminiClient = Client.builder()
                .apiKey("AIzaSyCGIDNDmAGNA9OweEohN5RU5U2utt8eYLo")
                .build();
    }

    public List<String> generateCategories(String productName, String productDescription, List<String> feedbacks) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze the following product feedback and create exactly 4 distinct category names.\n\n")
                .append("Product: ").append(productName).append("\n")
                .append("Description: ").append(productDescription).append("\n\n")
                .append("Feedback samples:\n");

        for (int i = 0; i < Math.min(feedbacks.size(), 8); i++) {
            prompt.append("- ").append(feedbacks.get(i)).append("\n");
        }

        prompt.append("\nReturn the response ONLY in valid JSON format with exactly 4 categories:")
                .append("\n{")
                .append("\n  \"categories\": [")
                .append("\n    \"Category Name 1\",")
                .append("\n    \"Category Name 2\",")
                .append("\n    \"Category Name 3\",")
                .append("\n    \"Category Name 4\"")
                .append("\n  ]")
                .append("\n}")
                .append("\n\nRequirements:")
                .append("\n- Each category should be 2-5 words maximum")
                .append("\n- Categories must be specific to the feedback content")
                .append("\n- Return ONLY valid JSON, no additional text")
                .append("\n- Do not include explanations or descriptions");

        try {
            GenerateContentResponse response = geminiClient.models.generateContent(
                    "gemini-2.5-flash",
                    prompt.toString(),
                    null
            );

            String text = response.text().trim();
            log.info("Raw JSON response: {}", text);

            return parseJsonCategories(text);

        } catch (Exception e) {
            log.error("Error generating categories: {}", e.getMessage(), e);
            return getDefaultCategories();
        }
    }



    private List<String> parseJsonCategories(String jsonText) {
        try {
            // Clean the response in case there's extra text
            String cleanJson = extractJsonFromResponse(jsonText);

            // Parse JSON
            JsonNode jsonNode = objectMapper.readTree(cleanJson);
            JsonNode categoriesArray = jsonNode.get("categories");

            if (categoriesArray != null && categoriesArray.isArray()) {
                List<String> categories = new ArrayList<>();
                for (JsonNode categoryNode : categoriesArray) {
                    categories.add(categoryNode.asText());
                }

                if (categories.size() == 4) {
                    log.info("Successfully parsed JSON categories: {}", categories);
                    return categories;
                } else {
                    log.warn("JSON contained {} categories instead of 4", categories.size());
                }
            }
        } catch (Exception e) {
            log.error("Failed to parse JSON response: {}", e.getMessage());
            log.debug("Failed JSON content: {}", jsonText);
        }

        return getDefaultCategories();
    }

    private String extractJsonFromResponse(String response) {
        // Remove any markdown formatting or extra text
        response = response.trim();

        // Find JSON boundaries
        int startIndex = response.indexOf("{");
        int endIndex = response.lastIndexOf("}");

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return response.substring(startIndex, endIndex + 1);
        }

        return response;
    }


    private List<String> getDefaultCategories() {
        return List.of(
                "Product Features & Functionality",
                "User Experience & Interface",
                "Performance & Reliability",
                "Quality & Build"
        );
    }

}











