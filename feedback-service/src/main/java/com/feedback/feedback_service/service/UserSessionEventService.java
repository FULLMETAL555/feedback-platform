package com.feedback.feedback_service.service;

import com.feedback.feedback_service.model.UserSessionEvent;
import com.feedback.feedback_service.repository.UserSessionEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSessionEventService {

    @Autowired
    private UserSessionEventRepository userSessionEventRepository;

    public UserSessionEvent save(UserSessionEvent event){
        return userSessionEventRepository.save(event);
    }

}
