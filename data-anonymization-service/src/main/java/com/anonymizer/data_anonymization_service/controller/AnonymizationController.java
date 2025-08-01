package com.anonymizer.data_anonymization_service.controller;

import com.anonymizer.data_anonymization_service.service.AnonymizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/anonymize")
public class AnonymizationController {

    @Autowired
    private AnonymizeService anonymizeService;

    @PostMapping
    public String anonymize(@RequestBody Map<String, String> request) {
        return anonymizeService.anonymizeFeedback(request);
    }

}
