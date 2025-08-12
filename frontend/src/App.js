// ==========================
// App.js - Main Application Component
// Handles all page routing
// ==========================

import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

// ----- Import pages -----
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import SignUpPage from "./pages/SignUpPage";
import Dashboard from "./pages/Dashboard";
import APIDocs from "./pages/APIDocs";
import ContactPage from "./pages/ContactPage";
import AuthRoute from "./components/common/AuthRoute";
import Products from "./pages/Products";

// ----- Import global and brand styles -----
import "./styles/respondit-theme.css"; // Brand colors, typography, buttons
import "./styles/globals.css"; // Global resets, utilities

function App() {
  return (
    // Wrap the whole app in the Router so we can use page navigation
    <Router>
      <Routes>
        {/* ----- Public Routes ----- */}
        <Route path="/" element={<HomePage />} /> {/* Landing page */}
        <Route path="/login" element={<LoginPage />} /> {/* Login page */}
        <Route path="/signup" element={<SignUpPage />} />{" "}
        {/* Registration page */}
        <Route path="/contact" element={<ContactPage />} />
        {/* Contact form */}
        {/* ----- Private/Protected Routes (later will add auth check) ----- */}
        <Route
          path="/dashboard"
          element={
            <AuthRoute>
              <Dashboard />
            </AuthRoute>
          }
        />
        {/* Main dashboard after login */}
        <Route
          path="/api-docs"
          element={
            <AuthRoute>
              <APIDocs />
            </AuthRoute>
          }
        />{" "}
        <Route
          path="/products"
          element={
            <AuthRoute>
              <Products />
            </AuthRoute>
          }
        />
        {/* API documentation and testing */}
        {/* ----- Fallback: redirect unknown URLs to home ----- */}
        <Route path="*" element={<HomePage />} />
      </Routes>
    </Router>
  );
}

export default App;
