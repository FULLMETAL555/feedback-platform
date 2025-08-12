// =========================================
// SignUpPage.js - Polished RESPONDIT Signup
// Modern, clean, on-brand style
// =========================================

import React from "react";
import Header from "../components/common/Header";
import Footer from "../components/common/Footer";
import SignUpForm from "../components/auth/SignUpForm";

function SignUpPage() {
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
        <div className="card" style={{ maxWidth: 420, width: "100%" }}>
          <h1
            style={{
              textAlign: "center",
              color: "var(--respondit-brown)",
              fontWeight: 600,
              fontSize: "2rem",
              letterSpacing: ".8px",
              marginBottom: "1rem",
            }}
          >
            Create Your RESPONDIT Account
          </h1>
          <p
            style={{
              textAlign: "center",
              color: "#666",
              marginBottom: "2rem",
              fontSize: "1.07rem",
            }}
          >
            Sign up to unlock your dashboard, manage products, and get
            AI-powered feedback insights.
          </p>
          <SignUpForm />
        </div>
        <div
          style={{
            marginTop: "2rem",
            color: "#493324",
            fontSize: "1rem",
            textAlign: "center",
          }}
        >
          Already have an account?
          <a
            href="/login"
            style={{
              color: "var(--respondit-gold)",
              fontWeight: 500,
              marginLeft: "0.4em",
              textDecoration: "underline",
            }}
          >
            Log in instead
          </a>
        </div>
      </main>

      <Footer />
    </div>
  );
}

export default SignUpPage;
