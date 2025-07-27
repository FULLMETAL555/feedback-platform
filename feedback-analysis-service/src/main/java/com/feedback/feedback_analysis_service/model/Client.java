package com.feedback.feedback_analysis_service.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import com.feedback.feedback_analysis_service.model.Client;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String clientId;

    @Column(unique = true, nullable = false)
    private String apiKey;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private boolean active = true;

    private String hashedApiKey;
    private LocalDateTime apiKeyCreatedAt;


    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    private List<Product> products;

    @PrePersist
    public void prePersist() {
        this.clientId = UUID.randomUUID().toString();
    }
    public LocalDateTime getApiKeyCreatedAt() {
        return apiKeyCreatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getHashedApiKey() {
        return hashedApiKey;
    }

    public void setApiKeyCreatedAt(LocalDateTime apiKeyCreatedAt) {
        this.apiKeyCreatedAt = apiKeyCreatedAt;
    }

    public void setHashedApiKey(String hashedApiKey) {
        this.hashedApiKey = hashedApiKey;
    }

    private String generateApiKey() {
        return UUID.randomUUID().toString().replace("-","") +UUID.randomUUID().toString().substring(0,8);
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getClientId() {
        return clientId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
