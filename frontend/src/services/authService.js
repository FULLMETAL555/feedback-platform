// ====================================
// authService.js - Clean, robust Auth Service
// ====================================

import api from "./api"; // axios instance

const authService = {
  signup: async (name, email, password) => {
    try {
      const res = await api.post("/auth/signup", { name, email, password });
      // Depending on your backend, you may want to auto-login here by saving session
      // For now, just return the response data (e.g. { apiKey: "xxx" })
      return res.data;
    } catch (err) {
      throw new Error(err.response?.data?.message || "Sign up failed");
    }
  },

  login: async (email, password) => {
    try {
      console.log("API base URL:", process.env.REACT_APP_API_BASE_URL);

      // Make sure this path matches your backend; adjust "/auth/signin" if needed
      const res = await api.post("/auth/signin", { email, password });
      const data = res.data;

      // Wrap flat user info in a user object for consistency
      const { token, clientId, email: userEmail, name, apikey } = data;
      const user = { clientId, email: userEmail, name, apikey };

      return { token, user };
    } catch (err) {
      throw new Error(err.response?.data?.message || "Login failed");
    }
  },

  saveSession: (token, user) => {
    localStorage.setItem("authToken", token);
    localStorage.setItem("authUser", JSON.stringify(user));
    localStorage.setItem("respondit_api_key", user.apikey);
    console.log(localStorage.getItem("respondit_api_key"));
  },

  logout: () => {
    localStorage.removeItem("authToken");
    localStorage.removeItem("authUser");
    localStorage.removeItem("respondit_api_key");
  },

  getToken: () => localStorage.getItem("authToken"),

  getUser: () => {
    const user = localStorage.getItem("authUser");
    return user ? JSON.parse(user) : null;
  },
};

export default authService;
