package com.feedback.feedback_service.repository;


import com.feedback.feedback_service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByProductId(Long productId);
    Optional<Category> findByNameAndProductId(String name, Long productId);

    void deleteByProductId(Long id);

    List<Category> findByProduct_Client_Id(Long clientId);

    @Query("""
        SELECT COUNT(f) 
        FROM Feedback f 
        WHERE f.category.id = :categoryId
    """)
    long countFeedbackForCategory(Long categoryId);

    @Query("""
        SELECT (COUNT(f) * 100.0 / 
               (SELECT COUNT(f2) FROM Feedback f2 WHERE f2.category.id = :categoryId))
        FROM Feedback f
        WHERE f.category.id = :categoryId
        AND f.sentiment = 'positive'
    """)
    Double getPositivePercentageForCategory(Long categoryId);
}
