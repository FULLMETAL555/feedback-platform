package com.feedback.feedback_service.repository;


import com.feedback.feedback_service.model.CategorySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorySummaryRepository extends JpaRepository<CategorySummary, Long> {
    List<CategorySummary> findByProductId(Long id);

    void deleteAllByProductId(Long id);

    Optional<CategorySummary> findByCategoryId(Long categoryId);}
