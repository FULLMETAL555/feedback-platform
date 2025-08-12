// HomePage.js

import React from "react";
import Header from "../components/common/Header";
import Footer from "../components/common/Footer";

function HomePage() {
  return (
    <div>
      <Header />

      {/* Hero Section: dark background */}
      <section
        style={{
          background: "var(--respondit-dark)",
          color: "var(--respondit-gold)",
          padding: "6rem 0",
          textAlign: "center",
        }}
      >
        <div className="container" style={{ maxWidth: 850 }}>
          <div
            className="respondit-logo"
            style={{ fontSize: "4rem", marginBottom: "2rem", letterSpacing: 8 }}
          >
            RESPONDIT
          </div>
          <h1
            style={{
              fontWeight: 600,
              fontSize: "3rem",
              lineHeight: 1.2,
              marginBottom: "1.5rem",
            }}
          >
            Transform Feedback <br /> Into{" "}
            <span style={{ color: "var(--respondit-gold)" }}>
              Actionable Insights
            </span>
          </h1>
          <p style={{ fontSize: "1.2rem", opacity: 0.9, marginBottom: "2rem" }}>
            RESPONDIT uses advanced AI to automatically categorize and summarize
            your customer feedback, so you can make smarter decisions faster.
          </p>

          <div
            style={{
              display: "flex",
              justifyContent: "center",
              gap: "1.8rem",
              flexWrap: "wrap",
            }}
          >
            <a href="/signup" className="btn btn-primary btn-large">
              Start Free Trial
            </a>
            <a
              href="/contact"
              className="btn btn-secondary btn-large"
              style={{ borderWidth: "2px" }}
            >
              Contact Us
            </a>
          </div>
        </div>
      </section>

      {/* Features Section: light background */}
      <section
        style={{ background: "var(--respondit-light)", padding: "4rem 0" }}
      >
        <div
          className="container"
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))",
            gap: "2.5rem",
            maxWidth: 1100,
          }}
        >
          {/* Feature Cards */}
          <div
            className="card"
            style={{ textAlign: "center", borderRadius: 18 }}
          >
            <div style={{ fontSize: "3rem", marginBottom: "1rem" }}>🤖</div>
            <h3>AI-Powered Analysis</h3>
            <p>
              Automatically categorize and analyze feedback using advanced AI.
            </p>
          </div>
          <div
            className="card"
            style={{ textAlign: "center", borderRadius: 18 }}
          >
            <div style={{ fontSize: "3rem", marginBottom: "1rem" }}>📊</div>
            <h3>Real-time Insights</h3>
            <p>View instant summaries and trends from your customers.</p>
          </div>
          <div
            className="card"
            style={{ textAlign: "center", borderRadius: 18 }}
          >
            <div style={{ fontSize: "3rem", marginBottom: "1rem" }}>🔒</div>
            <h3>Secure & Scalable</h3>
            <p>Enterprise-grade security and multi-tenant architecture.</p>
          </div>
        </div>
      </section>

      <Footer />
    </div>
  );
}

export default HomePage;
