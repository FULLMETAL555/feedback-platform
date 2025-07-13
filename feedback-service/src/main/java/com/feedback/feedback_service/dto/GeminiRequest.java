package com.feedback.feedback_service.dto;



import lombok.Data;

import java.util.List;

@Data
public class GeminiRequest {
    private List<Content> contents;

    @Data
    public static class Content {
        private List<Part> parts;

        private String role="user";

        @Data
        public static class Part{
            private String text;
        }

    }
}
