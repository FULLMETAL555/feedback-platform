import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:9099/api" || process.env.REACT_APP_API_BASE_URL, // backend URL
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
