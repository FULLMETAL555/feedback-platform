package com.feedback.feedback_analysis_service.controller;

import com.feedback.feedback_analysis_service.job.FeedbackAnalysisScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/debug")
@Slf4j
public class DebugController {

    @Autowired
    private FeedbackAnalysisScheduler scheduler;

    @PostMapping("/run-feedback-analysis")
    public ResponseEntity<String> runFeedbackAnalysis() {
        log.info("🔄 Manual trigger: Starting feedback analysis");
        try {
            // This calls the SAME method that runs on schedule
            scheduler.runAnalysisForNewFeedbackProducts();
            log.info("✅ Manual trigger: Analysis completed");
            return ResponseEntity.ok("✅ Feedback analysis completed successfully");
        } catch (Exception e) {
            log.error("❌ Manual trigger: Analysis failed", e);
            return ResponseEntity.status(500)
                    .body("❌ Analysis failed: " + e.getMessage());
        }
    }
}
