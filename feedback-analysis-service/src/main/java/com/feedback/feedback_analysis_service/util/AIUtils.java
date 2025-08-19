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

    public AIUtils(@Value("${google.api.key}") String apiKey) {
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
            log.info(prompt);

            int retry=3;

        while(retry-- >0){
            try {
                GenerateContentResponse response = geminiClient.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        null
                );

                String result = response.text();
                System.out.println("RESULT :" + result);
                return result != null ? result.trim() : "No Match";
            } catch (Exception e) {
                log.error("Error classifying feedback: {}", e.getMessage(), e);
                if (e.getMessage() != null && e.getMessage().contains("429") && retry > 0) {
                    try {
                        log.warn("Rate limit hit. Waiting 15 seconds before continuing...");
                        Thread.sleep(15000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

            }
        }
        return "No Match";
    }


    public String generateSummary(String prompt) {
        try {
            GenerateContentResponse response = geminiClient.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    null
            );

            String text = response.text();
            return text != null ? text.trim() : "No summary generated.";
        } catch (Exception e) {
            log.error("Error generating summary: {}", e.getMessage(), e);
            throw new RuntimeException("Summary generation failed", e);
        }
    }

    /**
     * Generate incremental summary by combining existing summary with new feedbacks
     */
    public String generateIncrementalSummary(String categoryName, List<FeedbackDTO> categoryFeedbacks, String existingSummary) {

        if (categoryFeedbacks.isEmpty()) {
            return existingSummary; // Return existing if no new relevant feedbacks
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("You have an existing summary for the category '").append(categoryName).append("':\n")
                .append("EXISTING SUMMARY: ").append(existingSummary).append("\n\n")
                .append("Now you have ").append(categoryFeedbacks.size()).append(" total feedbacks for this category:\n");

        for (int i = 0; i < categoryFeedbacks.size(); i++) {
            prompt.append((i + 1)).append(". ").append(categoryFeedbacks.get(i).getMessage()).append("\n");
        }

        prompt.append("\nTask: Create an updated summary that:")
                .append("\n- Incorporates insights from all feedbacks (including any new ones)")
                .append("\n- Maintains key insights from the existing summary if still relevant")
                .append("\n- Highlights any new trends or issues that have emerged")
                .append("\n- Keeps the summary concise (2-3 lines)")
                .append("\n\nReturn only the updated summary text, no explanations.");

        int retry=3;
        while(retry-- >0){
            try {
                GenerateContentResponse response = geminiClient.models.generateContent(
                        "gemini-2.5-flash",
                        prompt.toString(),
                        null
                );

                String updatedSummary = response.text();

                return updatedSummary != null ? updatedSummary.trim() : existingSummary;
            } catch (Exception e) {
                log.error("Error generating incremental summary for category '{}': {}", categoryName, e.getMessage(), e);
                if(e.getMessage()!=null || e.getMessage().contains("429") && retry>0 ){
                    try {
                        log.warn("Rate limit again hit. Waiting 15 seconds before continuing...");
                        Thread.sleep(30000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

            }
        }
        return existingSummary; // Return existing summary on error
    }

}
