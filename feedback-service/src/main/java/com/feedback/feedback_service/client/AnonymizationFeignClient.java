package com.feedback.feedback_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "anonymization-service", url = "${anonymization.service.url}")
public interface AnonymizationFeignClient {

    @PostMapping(consumes = "application/json", produces = "application/json")
    String anonymize(@RequestBody Map<String, String> request);
}
