package com.feedback.feedback_analysis_service.util;

import com.feedback.feedback_analysis_service.dto.FeedbackDTO;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AIUtils {

    private final Client geminiClient;

    public AIUtils(@Value("${GOOGLE_API_KEY}") String apiKey) {
        this.geminiClient = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    /**
     * Classify a feedback message into one of the given categories.
     * If none match, return "No Match".
     */
    public String classifyFeedbackIntoCategory(List<String> categories, String feedbackText) {
        String prompt = """
            You are a helpful assistant for customer feedback analysis.

            Given the following feedback:
            "%s"

            And the following categories:
            %s

            Select the best-matching category from the list above.

            If none of the categories match the feedback meaningfully, reply with: "No Match".
            Only return the category name or "No Match".
            """.formatted(feedbackText, String.join(", ", categories));

        try {
            GenerateContentResponse response = geminiClient.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    null
            );

            String result = response.text();
            return result != null ? result.trim() : "No Match";
        } catch (Exception e) {
            log.error("Error classifying feedback: {}", e.getMessage(), e);
            return "No Match";
        }
    }

    /**
     * Summarizes all feedbacks that are relevant to a given category.
     */
    public String summerizeFeedbackForCategory(String category, List<FeedbackDTO> feedbacks) {
        List<String> messages = feedbacks.stream()
                .filter(fb -> fb.getMessage().toLowerCase().contains(category.toLowerCase()))
                .map(FeedbackDTO::getMessage)
                .collect(Collectors.toList());

        if (messages.isEmpty()) {
            return "No specific feedback available for this category.";
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("Summarize the following user feedbacks for the category: ")
                .append(category).append(".\n")
                .append("Feedbacks:\n");

        for (String msg : messages) {
            prompt.append("- ").append(msg).append("\n");
        }

        prompt.append("\nProvide a concise 2-3 line summary that captures the main sentiment, issues, or praise.");

        try {
            GenerateContentResponse response = geminiClient.models.generateContent(
                    "gemini-2.5-flash",
                    prompt.toString(),
                    null
            );

            String text = response.text();
            return text != null ? text.trim() : "No summary generated.";
        } catch (Exception e) {
            log.error("Error summarizing feedback for category '{}': {}", category, e.getMessage(), e);
            return "Summary not available due to an error.";
        }
    }
}
