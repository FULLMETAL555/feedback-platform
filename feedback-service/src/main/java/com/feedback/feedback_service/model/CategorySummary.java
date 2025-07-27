package com.feedback.feedback_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class CategorySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String summaryText;

    private LocalDateTime generatedAt;

    @OneToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    @PrePersist
    public void prePersist() {
        this.generatedAt = LocalDateTime.now();
    }

    public String getSummaryText() {
        return summaryText;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public Category getCategory() {
        return category;
    }

    public Long getId() {
        return id;
    }

    public void setSummaryText(String summaryText) {
        this.summaryText = summaryText;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
