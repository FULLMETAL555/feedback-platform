package com.feedback.feedback_analysis_service.model;


import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.model.Feedback;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "client_id"})
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "product",cascade =CascadeType.ALL)
    private List<Feedback> feedbackList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Category> categories;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Client getClient() {
        return client;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
