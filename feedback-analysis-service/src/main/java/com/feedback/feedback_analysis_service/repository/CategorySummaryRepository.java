package com.feedback.feedback_analysis_service.repository;

import com.feedback.feedback_analysis_service.model.CategorySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorySummaryRepository extends JpaRepository<CategorySummary, Long> {
    List<CategorySummary> findByProductId(Long id);

    void deleteAllByProductId(Long id);
}
