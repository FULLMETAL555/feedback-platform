package com.feedback.feedback_service.controller;

import com.feedback.feedback_service.dto.*;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/kpi")
    public KpiStatsDTO getKpiStats(@AuthenticationPrincipal Client client) {
        return dashboardService.getKpiStats(client.getId());
    }

    @GetMapping("/trends")
    public List<TrendPointDTO> getTrends(@AuthenticationPrincipal Client client,
                                         @RequestParam(defaultValue = "month") String interval) {
        return dashboardService.getTrends(client.getId(), interval);
    }

    @GetMapping("/sentiment-distribution")
    public List<SentimentDistributionDTO> getSentimentDistribution(@AuthenticationPrincipal Client client) {
        return dashboardService.getSentimentDistribution(client.getId());
    }

    @GetMapping("/category-insights")
    public List<CategoryInsightDTO> getCategoryInsights(@AuthenticationPrincipal Client client) {
        return dashboardService.getCategoryInsights(client.getId());
    }

    @GetMapping("/product-performance")
    public List<ProductPerformanceDTO> getProductPerformance(@AuthenticationPrincipal Client client) {
        return dashboardService.getProductPerformance(client.getId());
    }

    @GetMapping("/recent-feedback")
    public List<RecentFeedbackDTO> getRecentFeedback(@AuthenticationPrincipal Client client,
                                                     @RequestParam(defaultValue = "10") int limit) {
        return dashboardService.getRecentFeedback(client.getId(), limit);
    }
}
