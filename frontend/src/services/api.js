import axios from "axios";
// process.env.REACT_APP_API_BASE_URL || "/api",
const api = axios.create({
  baseURL: "/api",
  headers: { "Content-Type": "application/json" },
});

// Attach API key from localStorage to every request
api.interceptors.request.use((config) => {
  const apiKey = localStorage.getItem("respondit_api_key");
  if (apiKey) {
    config.headers["X-API-Key"] = apiKey;
  }
  return config;
});

export default api;
