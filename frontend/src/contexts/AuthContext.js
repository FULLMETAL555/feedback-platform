import React, { createContext, useContext, useState, useEffect } from "react";
import authService from "../services/authService";

// Create the Context
const AuthContext = createContext();

// Provider component to wrap your app and provide auth state
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  // On mount, read user from localStorage
  useEffect(() => {
    const currentUser = authService.getUser();
    setUser(currentUser);
  }, []);

  // Login function updates context state
  const login = (token, userData) => {
    authService.saveSession(token, userData);
    setUser(userData);
  };

  // Logout function clears session and context state
  const logout = () => {
    authService.logout();
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

// Helper hook to consume the context easily in components
export function useAuth() {
  return useContext(AuthContext);
}
