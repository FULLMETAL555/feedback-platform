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
                .apiKey("AIzaSyCGIDNDmAGNA9OweEohN5RU5U2utt8eYLo")
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
    public String summarizeFeedbackForCategory(String category, List<FeedbackDTO> feedbacks) {
        // First classify feedbacks, then filter by category
        List<String> categorizedMessages = feedbacks.stream()
                .filter(fb -> {
                    String classified = classifyFeedbackIntoCategory(
                            List.of(category),
                            fb.getMessage()
                    );
                    return category.equalsIgnoreCase(classified.trim());
                })
                .map(FeedbackDTO::getMessage)
                .collect(Collectors.toList());

        if (categorizedMessages.isEmpty()) {
            return "No specific feedback available for this category.";
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("Summarize the following user feedbacks for the category: ")
                .append(category).append(".\n")
                .append("Feedbacks:\n");

        for (String msg : categorizedMessages) {
            prompt.append("- ").append(msg).append("\n");
        }

        prompt.append("\nProvide a concise 2-3 line summary highlighting key points, common themes, and overall sentiment.");

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
            return existingSummary; // Return existing summary on error
        }
    }

}
