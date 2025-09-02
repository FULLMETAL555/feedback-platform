// ==============================================
// apiKeyService.js - API key operations
// ==============================================

import api from "./api"; // axios instance

export const apiKeyService = {
  getCurrentApiKey: async () => {
    const res = await api.get("/clients/me");
    return res.data;
  },
  generateNewApiKey: async () => {
    const res = await api.post("/clients/generate");
    return res.data;
  },
};
