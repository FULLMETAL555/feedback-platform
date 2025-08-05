import axios from "axios";

const API_BASE_URL = "http://localhost:9099/api";

// Create axios instance with default settings
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

//Add API key to every request
api.interceptors.request.use((config) => {
  const apiKey = localStorage.getItem("respondit_api_key");
});
