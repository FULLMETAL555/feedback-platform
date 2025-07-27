package com.feedback.feedback_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // e.g. "UI/UX Issues", "Performance", etc.

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToOne(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private CategorySummary summary;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Product getProduct() {
        return product;
    }

    public CategorySummary getSummary() {
        return summary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setSummary(CategorySummary summary) {
        this.summary = summary;
    }
}
