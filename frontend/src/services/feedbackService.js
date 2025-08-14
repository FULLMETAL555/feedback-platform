// src/services/feedbackService.js
import api from "./api";

export const feedbackService = {
  getRecentStats: async () => {
    const apiKey = localStorage.getItem("respondit_api_key");
    const res = await api.get("/insights/api/feedback/stats/recent", {
      headers: { "X-API-KEY": apiKey },
    });
    return res.data;
  },

  getProductSummaries: async () => {
    const apiKey = localStorage.getItem("respondit_api_key");
    const res = await api.get("/insights/dashboard", {
      headers: { "X-API-KEY": apiKey },
    });
    return res.data;
  },

  getInsightSummary: async () => {
    const apiKey = localStorage.getItem("respondit_api_key");
    const res = await api.get("/insights/summary", {
      headers: { "X-API-KEY": apiKey },
    });
    return res.data;
  },

  getFeedbackTrends: async () => {
    const apiKey = localStorage.getItem("respondit_api_key");
    // Adjust when you have endpoint implemented
    return null;
  },
};
