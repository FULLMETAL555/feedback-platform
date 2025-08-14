package com.feedback.feedback_analysis_service.model;


import com.feedback.feedback_analysis_service.model.Product;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity

public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String type;

    private String message;

    private String submittedBy;

    private LocalDateTime submittedAt;

    private String sentiment;

    @ManyToOne
    @JoinColumn(name="product_id",nullable = false)
    private Product product;

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // or just Long categoryId if you prefer pure ID

    @PrePersist
    public void prePersist(){
        this.submittedAt = LocalDateTime.now();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public String getSentiment() {
        return sentiment;
    }

    public Product getProduct() {
        return product;
    }
}
