package com.feedback.feedback_service.repository;


import com.feedback.feedback_service.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByApiKey(String apiKey);
    Optional<Client> findByEmail(String email);
    Optional<Client> findByHashedApiKey(String hashedApiKey);

}