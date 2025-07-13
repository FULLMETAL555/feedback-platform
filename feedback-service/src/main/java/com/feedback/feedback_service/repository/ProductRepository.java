package com.feedback.feedback_service.repository;

import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByNameAndClient(String name, Client client);
}
