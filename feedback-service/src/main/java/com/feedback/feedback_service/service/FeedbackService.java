package com.feedback.feedback_service.service;


import com.feedback.feedback_service.dto.*;
import com.feedback.feedback_service.model.Category;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.model.Feedback;
import com.feedback.feedback_service.model.Product;
import com.feedback.feedback_service.repository.CategoryRepository;
import com.feedback.feedback_service.repository.ClientRepository;
import com.feedback.feedback_service.repository.FeedbackRepository;
import com.feedback.feedback_service.repository.ProductRepository;
import com.feedback.feedback_service.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GeminiSentimentService geminiSentimentService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private AnonymizationClient anonymizationClient;

    @Autowired
    private CategoryRepository categoryRepository;

    public FeedbackResponseDTO saveFeedback(FeedbackRequestDTO dto, Product product){
        Feedback feedback=new Feedback();
        feedback.setType(dto.getType());
//        /anon message
        String anonMesg=anonymizationClient.anonymize(dto.getMessage());
        feedback.setMessage(anonMesg);
        feedback.setSubmittedBy(dto.getSubmittedBy());
        feedback.setProduct(product);

        //AI sentiment
        String sentiment = geminiSentimentService.analyzeSentiment(dto.getMessage());
        feedback.setSentiment(sentiment);

        //Alert
        alertService.checkAndTriggerAlert(feedback);

         feedbackRepository.save(feedback);
         return toFeedbackResponseDTO(feedback);
    }

    public List<Feedback> getFeedbacksByProductAndApiKey(Long productId,String apiKey){

        Product product= productRepository.findById(productId).orElseThrow(()-> new RuntimeException("INVALID Product ID"));

        if(!product.getClient().getHashedApiKey().equals(HashUtil.hashApiKey(apiKey))){
            throw new RuntimeException("UNAUTHORIZED: wrong key");
        }

        return feedbackRepository.findByProductId(productId);

    }
    public List<Feedback> getFilteredFeedbacks(String sentiment, Long productId, Pageable pageable, String apiKey) {
        String hashApiKey= HashUtil.hashApiKey(apiKey);
        Client client=clientRepository.findByHashedApiKey(hashApiKey).orElseThrow(()-> new RuntimeException("UNAUTHORIZED"));
        return feedbackRepository.findBySentimentAndProductId(sentiment, productId,pageable);
    }

    public List<FeedbackSentimentSummary> getFeedbackSummaryByProduct(Long productId,String apiKey) {
        String hashApiKey= HashUtil.hashApiKey(apiKey);
        Client client=clientRepository.findByHashedApiKey(hashApiKey).orElseThrow(()-> new RuntimeException("UNAUTHORIZED"));
        return feedbackRepository.countBySentimentGroupedByProduct(productId);
    }

    public List<ProductFeedbackSummaryDTO> getProductSummaries(String apiKey){
        String hashApiKey= HashUtil.hashApiKey(apiKey);
        Client client=clientRepository.findByHashedApiKey(hashApiKey).orElseThrow(()-> new RuntimeException("UNAUTHORIZED"));
        return feedbackRepository.getProductSummariesByClientId(apiKey);
    }
    public FeedbackResponseDTO toFeedbackResponseDTO(Feedback feedback) {
        ProductResponseDTO productDTO = new ProductResponseDTO();
        productDTO.setId(feedback.getProduct().getId());
        productDTO.setName(feedback.getProduct().getName());
        productDTO.setDescription(feedback.getProduct().getDescription());
        productDTO.setClientId(feedback.getProduct().getClient().getId());

        FeedbackResponseDTO response = new FeedbackResponseDTO();
        response.setId(feedback.getId());
        response.setType(feedback.getType());
        response.setMessage(feedback.getMessage());
        response.setSubmittedBy(feedback.getSubmittedBy());
        response.setSubmittedAt(feedback.getSubmittedAt());
        response.setSentiment(feedback.getSentiment());
        response.setProduct(productDTO);

        return response;
    }

    public List<Feedback> getFeedbackByProductId(Long productId) {

          List<Feedback> feedback= feedbackRepository.findByProductId(productId);

        return feedback;

    }
    public ResponseEntity<Object> updateFeedbackCategory(Long id, Long categoryId) {
        return feedbackRepository.findById(id).map(feedback -> {
            Category categoryRef = categoryId!=null? categoryRepository.findById(categoryId).orElseThrow(()->new RuntimeException("Category not found")):null;
            feedback.setCategory(categoryRef);
            feedbackRepository.save(feedback);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    public List<FeedbackTrendsDTO> getTrendsForClient(String apiKey) {
        Client client=clientService.getClientByConvertingHashed(apiKey);
        List<Object[]> results = feedbackRepository.findFeedbackTrendsByClientId(client.getId());
        List<FeedbackTrendsDTO> trends = new ArrayList<>();
        for (Object[] row : results) {
            String period = (String) row[0];
            long count = ((Number) row[1]).longValue();
            double avg = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
            trends.add(new FeedbackTrendsDTO(period, count, avg));
        }
        return trends;
    }
}
