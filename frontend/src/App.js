import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

// Import pages
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import SignUpPage from "./pages/SignUpPage";
import Dashboard from "./pages/Dashboard";
import APIDocs from "./pages/APIDocs";
import ContactPage from "./pages/ContactPage";

// Import styles
import "./styles/globals.css";
import "./styles/respondit-theme.css";
import "./styles/components.css";

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/contact" element={<ContactPage />} />

          {/* Protected Routes (will add authentication later) */}
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/api-docs" element={<APIDocs />} />

          {/* Fallback route */}
          <Route path="*" element={<HomePage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
