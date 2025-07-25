package com.feedback.feedback_analysis_service.model;

import com.feedback.feedback_service.model.Product;
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


}
