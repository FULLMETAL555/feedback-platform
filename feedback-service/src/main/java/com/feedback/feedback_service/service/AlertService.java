package com.feedback.feedback_service.service;


import com.feedback.feedback_service.model.Feedback;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

@Service
public class AlertService {

    private static final Logger logger= LoggerFactory.getLogger(AlertService.class);

    public void checkAndTriggerAlert(Feedback feedback){
        if("Negative".equalsIgnoreCase(feedback.getSentiment())){
            logger.warn("Negative Feedback Alert for Product [{}]: {}", feedback.getProduct(),feedback.getMessage());

            // Optional future enhancement:
            // - Send email to client
            // - Push notification
            // - Trigger webhook, etc.
        }
    }


}
