// src/services/feedbackService.js
import api from "./api";

export const feedbackService = {
  // 1. KPI stats for top of dashboard
  getKpiStats: async () => {
    const apiKey = localStorage.getItem("respondit_api_key");
    console.log(apiKey);
    const res = await api.get("/dashboard/kpi", {
      headers: { "X-API-KEY": apiKey },
    });
    return res.data; // { totalFeedbacks, avgRating, productCount, positivePercentage, ... }
  },

  // 2. Trends data for line chart
  getFeedbackTrends: async (interval = "month") => {
    const apiKey = localStorage.getItem("respondit_api_key");
    const res = await api.get(`/dashboard/trends?interval=${interval}`, {
      headers: { "X-API-KEY": apiKey },
    });
    return res.data; // [{ period: "2025-01", feedbackCount: 20, averageRating: 4.2 }, ...]
  },

  // 3. Sentiment distribution for pie chart
  getSentimentDistribution: async () => {
    const apiKey = localStorage.getItem("respondit_api_key");
    const res = await api.get("/dashboard/sentiment-distribution", {
      headers: { "X-API-KEY": apiKey },
    });
    return res.data; // [{ sentiment: "Positive", count: 120 }, ...]
  },

  // 4. AI-generated category insights
  getCategoryInsights: async () => {
    const apiKey = localStorage.getItem("respondit_api_key");
    const res = await api.get("/dashboard/category-insights", {
      headers: { "X-API-KEY": apiKey },
    });
    return res.data; // [{ categoryName, summary, feedbackCount, averageRating }, ...]
  },

  // 5. Product performance table
  getProductPerformance: async () => {
    const apiKey = localStorage.getItem("respondit_api_key");
    const res = await api.get("/dashboard/product-performance", {
      headers: { "X-API-KEY": apiKey },
    });
    return res.data; // [{ productName, totalFeedback, avgRating, positivePercentage, lastFeedbackDate }, ...]
  },

  // 6. Recent feedback list
  getRecentFeedback: async (limit = 10) => {
    const apiKey = localStorage.getItem("respondit_api_key");
    const res = await api.get(`/dashboard/recent-feedback?limit=${limit}`, {
      headers: { "X-API-KEY": apiKey },
    });
    return res.data; // [{ submittedAt, productName, sentiment, message }, ...]
  },
  getClientFeedbackTrends: async () => {
    const apiKey = localStorage.getItem("respondit_api_key");
    const res = await api.get(`/feedback/client/trends`, {
      headers: { "X-API-KEY": apiKey },
    });
    return res.data;
  },
};

export default feedbackService;
