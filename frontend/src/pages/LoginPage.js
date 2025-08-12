// =========================================
// LoginPage.js - Polished RESPONDIT Login
// Clean, spacious, branded style
// =========================================

import React from "react";
import Header from "../components/common/Header";
import Footer from "../components/common/Footer";
import LoginForm from "../components/auth/LoginForm";

function LoginPage() {
  return (
    <div>
      <Header />

      <main
        className="container"
        style={{
          minHeight: "72vh",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          flexDirection: "column",
          padding: "3rem 0",
        }}
      >
        <div className="card" style={{ maxWidth: 410, width: "100%" }}>
          <h1
            style={{
              textAlign: "center",
              color: "var(--respondit-brown)",
              fontWeight: "600",
              fontSize: "2rem",
              letterSpacing: ".8px",
              marginBottom: "1rem",
            }}
          >
            Welcome Back
          </h1>
          <p
            style={{
              textAlign: "center",
              color: "#666",
              marginBottom: "2rem",
              fontSize: "1.07rem",
            }}
          >
            Log in to your RESPONDIT account to access your dashboard, manage
            products, and view insights.
          </p>
          <LoginForm />
        </div>
        <div
          style={{
            marginTop: "2rem",
            color: "#493324",
            fontSize: "1rem",
            textAlign: "center",
          }}
        >
          Don't have an account?
          <a
            href="/signup"
            style={{
              color: "var(--respondit-gold)",
              fontWeight: 500,
              marginLeft: "0.4em",
              textDecoration: "underline",
            }}
          >
            Sign up now
          </a>
        </div>
      </main>

      <Footer />
    </div>
  );
}

export default LoginPage;
