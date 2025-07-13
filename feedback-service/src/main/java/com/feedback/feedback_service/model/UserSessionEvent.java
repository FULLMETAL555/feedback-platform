package com.feedback.feedback_service.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class UserSessionEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private  String sessionId;
    private String eventType;
    private String details;
    private LocalDateTime eventTime;

    @PrePersist
    public void prePersist(){
        this.eventTime=LocalDateTime.now();
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
