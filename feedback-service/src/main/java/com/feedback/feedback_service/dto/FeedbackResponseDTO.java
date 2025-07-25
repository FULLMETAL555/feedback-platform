package com.feedback.feedback_service.dto;

import java.time.LocalDateTime;

public class FeedbackResponseDTO {
    private Long id;
    private String type;
    private String message;
    private String submittedBy;
    private LocalDateTime submittedAt;
    private String sentiment;
    private ProductResponseDTO product;


    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(String submittedBy) { this.submittedBy = submittedBy; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public String getSentiment() { return sentiment; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }

    public ProductResponseDTO getProduct() { return product; }
    public void setProduct(ProductResponseDTO product) { this.product = product; }
}
