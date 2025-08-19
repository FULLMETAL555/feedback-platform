package com.feedback.feedback_service.repository;

import com.feedback.feedback_service.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.product.client.id = :clientId")
    long countFeedbackByClient(Long clientId);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.client.id = :clientId")
    long countProductsByClient(Long clientId);

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.product.client.id = :clientId AND f.sentiment = 'Positive'")
    long countPositiveFeedbackByClient(Long clientId);

    @Query(value = """
        SELECT DATE_TRUNC(:interval, f.submitted_at) AS period,
               COUNT(*) AS total_count,
               SUM(CASE WHEN sentiment = 'Positive' THEN 1 ELSE 0 END) AS positive_count,
               SUM(CASE WHEN sentiment = 'Negative' THEN 1 ELSE 0 END) AS negative_count,
               SUM(CASE WHEN sentiment = 'Neutral' THEN 1 ELSE 0 END) AS neutral_count
        FROM feedback f
        JOIN product p ON f.product_id = p.id
        WHERE p.client_id = :clientId
        GROUP BY period ORDER BY period
    """, nativeQuery = true)
    List<Object[]> getFeedbackTrends(Long clientId, String interval);

    @Query("""
        SELECT f.sentiment, COUNT(f)
        FROM Feedback f
        WHERE f.product.client.id = :clientId
        GROUP BY f.sentiment
    """)
    List<Object[]> getSentimentDistribution(Long clientId);

    @Query("""
    SELECT p.name,
           COUNT(f),
           SUM(CASE WHEN f.sentiment = 'Positive' THEN 1 ELSE 0 END) * 100.0 / NULLIF(COUNT(f), 0),
           MAX(f.submittedAt)
    FROM Product p
    LEFT JOIN Feedback f ON f.product.id = p.id
    WHERE p.client.id = :clientId
    GROUP BY p.name
""")
    List<Object[]> getProductPerformance(Long clientId);

    @Query("""
        SELECT f.submittedAt, f.message, f.sentiment, p.name
        FROM Feedback f
        JOIN f.product p
        WHERE p.client.id = :clientId
        ORDER BY f.submittedAt DESC
    """)
    List<Object[]> getRecentFeedback(Long clientId, Pageable pageable);


}
