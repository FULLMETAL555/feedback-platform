// ==========================================
// AuthRoute.js - Protects routes behind login
// Redirects to /login if not authenticated
// ==========================================

import React from "react";
import { Navigate } from "react-router-dom";

function AuthRoute({ children }) {
  const apiKey = localStorage.getItem("respondit_api_key");
  if (!apiKey) {
    return <Navigate to="/login" replace />;
  }

  return children;
}

export default AuthRoute;
