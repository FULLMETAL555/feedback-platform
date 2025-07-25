package com.feedback.feedback_analysis_service.repository;

import com.feedback.feedback_analysis_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}
