package com.feedback.feedback_service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackRequestDTO {
    @NotBlank(message = "Feedback type is required")
    private String type;

    @NotBlank(message = "Message is required")
    @Size(max = 500, message = "Message must be under 500 characters")
    private String message;

    @NotBlank(message = "email or username is required")
    private String submittedBy;

    @NotBlank(message = "Product ID is required")
    private Long productId;

    public void setType(String type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public Long getProductId() {
        return productId;
    }
}
