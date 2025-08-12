// src/services/feedbackService.js

import api from "./api";

export const feedbackService = {
  getDashboardStats: async () => {
    const res = await api.get("/dashboard/stats");
    return res.data; // Expect { totalFeedback, activeProducts, averageRating, totalUsers }
  },

  getInsights: async (productId) => {
    // If productId is provided, get insights for that product
    const url = productId ? `/products/${productId}/insights` : `/insights`;
    const res = await api.get(url);
    return res.data;
  },
};
