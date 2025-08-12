// ====================================
// authService.js
// ====================================

import api from "./api"; // axios instance

export const authService = {
  signup: async (name, email, password) => {
    try {
      const res = await api.post("/auth/signup", { name, email, password });
      return res.data; // Expect { apiKey: "xxx" }
    } catch (err) {
      throw new Error(err.response?.data?.message || "Sign up failed");
    }
  },

  login: async (email, password) => {
    // Call the backend login API
    const res = await api.post("/auth/signin", { email, password });

    const data = res.data;

    // Wrap flat user fields in 'user' object for consistency
    const { token, clientId, email: userEmail, name, apikey } = data;

    // Construct user object explicitly
    const user = {
      clientId,
      email: userEmail,
      name,
      apikey,
    };

    // Return consistent structure
    return { token, user };
  },

  saveSession: (token, user) => {
    localStorage.setItem("authToken", token);
    localStorage.setItem("authUser", JSON.stringify(user));
    localStorage.setItem("respondit_api_key", user.apikey);
  },

  logout: () => {
    localStorage.removeItem("authToken");
    localStorage.removeItem("authUser");
  },

  getToken: () => localStorage.getItem("authToken"),

  getUser: () => {
    const user = localStorage.getItem("authUser");
    return user ? JSON.parse(user) : null;
  },
};

export default authService;
