package com.feedback.feedback_analysis_service.model;

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

    @PrePersist
    public void prePersist() {
        this.generatedAt = LocalDateTime.now();
    }


}

