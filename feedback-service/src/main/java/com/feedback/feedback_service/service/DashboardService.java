package com.feedback.feedback_service.service;

import com.feedback.feedback_service.dto.*;
import com.feedback.feedback_service.model.Category;
import com.feedback.feedback_service.repository.CategoryRepository;
import com.feedback.feedback_service.repository.CategorySummaryRepository;
import com.feedback.feedback_service.repository.DashboardRepository;
import com.feedback.feedback_service.model.CategorySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;


import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepo;
    private final CategoryRepository categoryRepo;
    private final CategorySummaryRepository summaryRepo;

    public KpiStatsDTO getKpiStats(Long clientId) {
        long total = dashboardRepo.countFeedbackByClient(clientId);
        long products = dashboardRepo.countProductsByClient(clientId);
        long positive = dashboardRepo.countPositiveFeedbackByClient(clientId);

        KpiStatsDTO dto = new KpiStatsDTO();
        dto.setTotalFeedbacks(total);
        dto.setNumberOfProducts(products);
        dto.setPositivePercentage(total > 0 ? (positive * 100.0) / total : 0.0);
        return dto;
    }

    public List<TrendPointDTO> getTrends(Long clientId, String interval) {
        return dashboardRepo.getFeedbackTrends(clientId, interval).stream().map(obj -> {
            TrendPointDTO dto = new TrendPointDTO();
            dto.setPeriod(obj[0].toString());
            dto.setTotalCount(((Number) obj[1]).longValue());
            dto.setPositiveCount(((Number) obj[2]).longValue());
            dto.setNegativeCount(((Number) obj[3]).longValue());
            dto.setNeutralCount(((Number) obj[4]).longValue());
            return dto;
        }).toList();
    }

    public List<SentimentDistributionDTO> getSentimentDistribution(Long clientId) {
        return dashboardRepo.getSentimentDistribution(clientId).stream().map(obj -> {
            SentimentDistributionDTO dto = new SentimentDistributionDTO();
            dto.setSentiment((String) obj[0]);
            dto.setCount(((Number) obj[1]).longValue());
            return dto;
        }).toList();
    }

    public List<CategoryInsightDTO> getCategoryInsights(Long clientId) {
        List<CategoryInsightDTO> list = new ArrayList<>();
        List<Category> categories = categoryRepo.findByProduct_Client_Id(clientId);
        for (Category cat : categories) {
            long feedbackCount = categoryRepo.countFeedbackForCategory(cat.getId());
            double positivePercentage = categoryRepo.getPositivePercentageForCategory(cat.getId());
            String summary = summaryRepo.findByCategoryId(cat.getId())
                    .map(CategorySummary::getSummaryText).orElse("No summary");
            CategoryInsightDTO dto = new CategoryInsightDTO();
            dto.setCategoryName(cat.getName());
            dto.setFeedbackCount(feedbackCount);
            dto.setPositivePercentage(positivePercentage);
            dto.setSummary(summary);
            list.add(dto);
        }
        return list;
    }

    public List<ProductPerformanceDTO> getProductPerformance(Long clientId) {
        return dashboardRepo.getProductPerformance(clientId).stream().map(obj -> {
            ProductPerformanceDTO dto = new ProductPerformanceDTO();
            dto.setProductName((String) obj[0]);
            dto.setTotalFeedback(((Number) obj[1]).longValue());
            dto.setPositivePercentage(((Number) obj[2]).doubleValue());
            dto.setLastFeedbackDate(obj[3] != null ? obj[3].toString() : null);
            return dto;
        }).toList();
    }

    public List<RecentFeedbackDTO> getRecentFeedback(Long clientId, int limit) {
        return dashboardRepo.getRecentFeedback(clientId, (Pageable) PageRequest.of(0, limit)).stream().map(obj -> {
            RecentFeedbackDTO dto = new RecentFeedbackDTO();
            dto.setDate(obj[0].toString());
            dto.setMessage((String) obj[1]);
            dto.setSentiment((String) obj[2]);
            dto.setProductName((String) obj[3]);
            return dto;
        }).toList();
    }
}

