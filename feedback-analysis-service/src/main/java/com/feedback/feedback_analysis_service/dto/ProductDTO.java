package com.feedback.feedback_analysis_service.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Long clientId;
}
