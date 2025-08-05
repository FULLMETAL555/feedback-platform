package com.feedback.feedback_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
public class Client implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    // ADD: Password field for authentication
    @Column(nullable = false)
    private String password; // This will store the hashed password

    @Column(unique = true, nullable = false)
    private String clientId;

    @Column(unique = true, nullable = false)
    private String apiKey;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private boolean active = true;

    private String hashedApiKey;
    private LocalDateTime apiKeyCreatedAt;

    // ADD: Account status fields for UserDetails implementation
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
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
