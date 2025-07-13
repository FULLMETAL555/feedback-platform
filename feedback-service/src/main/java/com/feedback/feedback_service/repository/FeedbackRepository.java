package com.feedback.feedback_service.repository;

import com.feedback.feedback_service.dto.FeedbackSentimentSummary;
import com.feedback.feedback_service.dto.ProductFeedbackSummaryDTO;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    List<Feedback> findByProductId(Long productId);
    List<Feedback> findBySentimentAndProductId(String sentiment, Long productId, Pageable pageable);

    @Query("SELECT new com.feedback.feedback_service.dto.FeedbackSentimentSummary(f.sentiment, COUNT(f)) FROM Feedback f WHERE f.product.id = :productId GROUP BY f.sentiment")
    List<FeedbackSentimentSummary> countBySentimentGroupedByProduct(@Param("productId") Long productId);

    @Query("SELECT new com.feedback.feedback_service.dto.ProductFeedbackSummaryDTO(" +
            "p.id, p.name, COUNT(f), MAX(f.submittedAt)) " +
            "FROM Feedback f JOIN f.product p " +
            "WHERE p.client.apiKey = :apiKey " +
            "GROUP BY p.id, p.name")
    List<ProductFeedbackSummaryDTO> getProductSummariesByClientId(@Param("apiKey") String apiKey);

    Optional<Feedback> findByProductIdAndSubmittedBy(Long productId, String submittedBy);

    @Query("SELECT f.sentiment, COUNT(f) " +
            "FROM Feedback f " +
            "JOIN f.product p " +
            "WHERE p.client = :client AND f.submittedAt >= :fromDate " +
            "GROUP BY f.sentiment")
    List<Object[]> countRecentFeedbacksBySentimentForClient(@Param("client") Client client, @Param("fromDate") LocalDateTime fromDate);
}
