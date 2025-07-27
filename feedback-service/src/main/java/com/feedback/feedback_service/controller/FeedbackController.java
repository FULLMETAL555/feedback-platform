package com.feedback.feedback_service.controller;

import com.feedback.feedback_service.dto.FeedbackRequestDTO;
import com.feedback.feedback_service.dto.FeedbackResponseDTO;
import com.feedback.feedback_service.dto.ProductResponseDTO;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.model.Feedback;
import com.feedback.feedback_service.model.Product;
import com.feedback.feedback_service.service.ClientService;
import com.feedback.feedback_service.service.FeedbackService;
import com.feedback.feedback_service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    @PostMapping("/submit")
    public FeedbackResponseDTO submitFeedback(@RequestHeader("X-API-KEY") String apiKey,@Valid @RequestBody FeedbackRequestDTO dto){

        //validate client
        Client client=clientService.getClientByConvertingHashed(apiKey);
        //validate product
        Product product=productService.findById(dto.getProductId());
        System.out.println("product client :"+product.getClient().getId());
        System.out.println("client :"+client.getId());
        if(!(Objects.equals(product.getClient().getId(), client.getId()))){
            throw new RuntimeException("Unauthorized to submit feedback for this product");
        }



        return feedbackService.saveFeedback(dto,product);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByProductId(@PathVariable("productId") Long productId){

        List<Feedback> feedbacks= feedbackService.getFeedbackByProductId(productId);
        List<FeedbackResponseDTO> feedbackResponseDTOS= feedbacks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(feedbackResponseDTOS);


    }


    private FeedbackResponseDTO convertToDTO(Feedback feedback) {
        FeedbackResponseDTO dto = new FeedbackResponseDTO();
        dto.setId(feedback.getId());
        dto.setType(feedback.getType());
        dto.setMessage(feedback.getMessage());
        dto.setSubmittedBy(feedback.getSubmittedBy());
        dto.setSubmittedAt(feedback.getSubmittedAt());
        dto.setSentiment(feedback.getSentiment());

        // Convert product if it exists
        if (feedback.getProduct() != null) {
            ProductResponseDTO productDTO = new ProductResponseDTO();
            productDTO.setId(feedback.getProduct().getId());
            productDTO.setName(feedback.getProduct().getName()); // Adjust based on your Product entity
            // Add other product fields as needed
            dto.setProduct(productDTO);
        }

        return dto;
    }




}
