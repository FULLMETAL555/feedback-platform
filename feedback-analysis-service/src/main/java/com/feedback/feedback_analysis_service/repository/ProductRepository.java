package com.feedback.feedback_analysis_service.repository;

import com.feedback.feedback_analysis_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p From Product p JOIN p.feedbackList f WHERE f.category IS NULL")
    List<Product> findProductsWithUncategorizedFeedback();
}
