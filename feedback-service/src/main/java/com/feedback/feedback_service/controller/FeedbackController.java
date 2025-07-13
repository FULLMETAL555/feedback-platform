package com.feedback.feedback_service.controller;

import com.feedback.feedback_service.dto.FeedbackRequestDTO;
import com.feedback.feedback_service.dto.FeedbackResponseDTO;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.model.Product;
import com.feedback.feedback_service.service.ClientService;
import com.feedback.feedback_service.service.FeedbackService;
import com.feedback.feedback_service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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





}
