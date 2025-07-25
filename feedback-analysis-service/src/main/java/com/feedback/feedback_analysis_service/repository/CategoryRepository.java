package com.feedback.feedback_analysis_service.repository;

import com.feedback.feedback_analysis_service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByProductId(Long productId);
    Optional<Category> findByNameAndProductId(String name, Long productId);
}
