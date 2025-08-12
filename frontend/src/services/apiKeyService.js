// ==============================================
// apiKeyService.js - API key operations
// ==============================================

import api from "./api"; // axios instance

export const apiKeyService = {
  getCurrentApiKey: async () => {
    const res = await api.get("/api-keys/current");
    return res.data;
  },
  generateNewApiKey: async () => {
    const res = await api.post("/api-keys/generate");
    return res.data;
  },
};
