package com.feedback.feedback_analysis_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class FeedbackAnalysisServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedbackAnalysisServiceApplication.class, args);
	}

}
