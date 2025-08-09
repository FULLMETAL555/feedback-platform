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

@Slf4j
@Component
public class CategoryGenerationUtil {

    private final Client geminiClient;

    @Autowired
    private ObjectMapper objectMapper;

    public CategoryGenerationUtil(@Value("${google.api.key}") String apiKey){
        this.geminiClient = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public List<String> generateCategories(String productName, String productDescription, List<String> feedbacks) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze the following product feedback and create a list of distinct category names that best cover the feedback.\n\n")
                .append("Product: ").append(productName).append("\n")
                .append("Description: ").append(productDescription).append("\n\n")
                .append("Feedback samples:\n");


        for (int i = 0; i < feedbacks.size(); i++) {
            prompt.append("- ").append(feedbacks.get(i)).append("\n");
        }

        prompt.append("\nReturn ONLY valid JSON in this format (with as many or as few categories as needed, no explanations):\n")
                .append("{\n  \"categories\": [\"Category 1\", \"Category 2\", \"Category 3\", ...]\n}\n")
                .append("\nRequirements:\n")
                .append("- Each category should be 2-5 words maximum.\n")
                .append("- Categories must directly reflect the main recurring themes in feedback.\n")
                .append("- Do not provide explanations or descriptions.\n");

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
            // Clean the response in case there's extra text or formatting
            String cleanJson = extractJsonFromResponse(jsonText);

            // Parse JSON content
            JsonNode jsonNode = objectMapper.readTree(cleanJson);
            JsonNode categoriesArray = jsonNode.get("categories");

            if (categoriesArray != null && categoriesArray.isArray()) {
                List<String> categories = new ArrayList<>();
                for (JsonNode categoryNode : categoriesArray) {
                    categories.add(categoryNode.asText());
                }
                log.info("Successfully parsed {} JSON categories: {}", categories.size(), categories);
                return categories;
            }
        } catch (Exception e) {
            log.error("Failed to parse JSON response: {}", e.getMessage());
            log.debug("Failed JSON content: {}", jsonText);
        }
        // Fallback to defaults if anything goes wrong
        return getDefaultCategories();
    }

    private String extractJsonFromResponse(String response) {
        response = response.trim();
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
