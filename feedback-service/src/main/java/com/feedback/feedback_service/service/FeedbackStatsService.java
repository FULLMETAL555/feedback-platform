package com.feedback.feedback_service.service;


import com.feedback.feedback_service.dto.FeedbackStatResponseDTO;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.repository.ClientRepository;
import com.feedback.feedback_service.repository.FeedbackRepository;
import com.feedback.feedback_service.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackStatsService {


    @Autowired
    private FeedbackService feedbackService;


    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ClientRepository clientRepository;

    public FeedbackStatResponseDTO getRecentFeedbackStats(String apiKey) {

        String hashApiKey= HashUtil.hashApiKey(apiKey);
        Client client=clientRepository.findByHashedApiKey(hashApiKey).orElseThrow(()-> new RuntimeException("UNAUTHORIZED"));

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Object[]> results = feedbackRepository.countRecentFeedbacksBySentimentForClient(client,sevenDaysAgo);

        long positive = 0, neutral = 0, negative = 0;
        for (Object[] row : results) {
            String sentiment = (String) row[0];
            Long count = (Long) row[1];

            switch (sentiment.toLowerCase()) {
                case "positive" -> positive = count;
                case "neutral" -> neutral = count;
                case "negative" -> negative = count;
            }
        }
        long total =positive+neutral+negative;

        return new FeedbackStatResponseDTO(total,positive, negative, neutral);
    }

}
