package com.feedback.feedback_analysis_service.util;


import com.feedback.feedback_analysis_service.dto.FeedbackDTO;
import com.feedback.feedback_analysis_service.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AIUtils {


    public List<String> generateCategories(ProductDTO product, List<FeedbackDTO> feedback){

        return List.of("UI","Performance","Support","Features");
    }

    public String summerizeFeedbackForCategory(String category,List<FeedbackDTO> feedbacks){
        List<String> messages = feedbacks.stream()
                .filter(fb -> fb.getMessage().toString())
    }
}
