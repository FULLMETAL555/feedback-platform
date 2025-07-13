package com.feedback.feedback_service.controller;

import com.feedback.feedback_service.model.UserSessionEvent;
import com.feedback.feedback_service.service.UserSessionEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class UserSessionEventController {

    @Autowired
    private UserSessionEventService service;

    @PostMapping
    public UserSessionEvent logEvent(@RequestBody UserSessionEvent event) {
        System.out.println("sessionId: " + event.getSessionId());
        System.out.println("eventType: " + event.getEventType());
        System.out.println("details: " + event.getDetails());
        return service.save(event);
    }
}
