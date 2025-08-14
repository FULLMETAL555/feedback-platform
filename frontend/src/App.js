// ==========================
// App.js - Main Application Component
// Handles all page routing
// ==========================

import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

// ----- Import context -----
import { AuthProvider } from "./contexts/AuthContext";

// ----- Import pages -----
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import SignUpPage from "./pages/SignUpPage";
import Dashboard from "./pages/Dashboard";
import APIDocs from "./pages/APIDocs";
import ContactPage from "./pages/ContactPage";
import Products from "./pages/Products";

// ----- Import common components -----
import AuthRoute from "./components/common/AuthRoute";

// ----- Import global and brand styles -----
import "./styles/respondit-theme.css"; // Brand colors, typography, buttons
import "./styles/globals.css"; // Global resets, utilities

function App() {
  return (
    // Provide auth state to the whole application
    <AuthProvider>
      {/* Wrap the whole app in the Router for navigation */}
      <Router>
        <Routes>
          {/* ----- Public Routes ----- */}
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/contact" element={<ContactPage />} />

          {/* ----- Private Routes protected by Auth ----- */}
          <Route
            path="/dashboard"
            element={
              <AuthRoute>
                <Dashboard />
              </AuthRoute>
            }
          />
          <Route
            path="/api-docs"
            element={
              <AuthRoute>
                <APIDocs />
              </AuthRoute>
            }
          />
          <Route
            path="/products"
            element={
              <AuthRoute>
                <Products />
              </AuthRoute>
            }
          />

          {/* ----- Fallback to HomePage for unknown URLs ----- */}
          <Route path="*" element={<HomePage />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
