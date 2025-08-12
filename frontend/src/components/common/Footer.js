// Footer.js

import React from "react";
import { Link } from "react-router-dom";

function Footer() {
  return (
    <footer
      style={{
        backgroundColor: "var(--respondit-dark)",
        color: "var(--respondit-light)",
        padding: "3rem 0 2rem",
        marginTop: "4rem",
        userSelect: "none",
      }}
    >
      <div
        className="container"
        style={{
          display: "flex",
          justifyContent: "space-between",
          flexWrap: "wrap",
          gap: "2rem",
        }}
      >
        <div>
          <div
            className="respondit-logo"
            style={{
              fontSize: "1.5rem",
              marginBottom: "1rem",
              color: "var(--respondit-gold)",
            }}
          >
            RESPONDIT
          </div>
          <p
            style={{
              maxWidth: 280,
              color: "var(--respondit-light)",
              fontSize: "1rem",
            }}
          >
            AI-Enabled Feedback & Insight Platform
          </p>
        </div>
        <div>
          <h4 style={{ color: "var(--respondit-gold)", marginBottom: "1rem" }}>
            Quick Links
          </h4>
          <ul style={{ listStyle: "none", paddingLeft: 0 }}>
            <li>
              <Link to="/api-docs" style={{ color: "var(--respondit-light)" }}>
                API Docs
              </Link>
            </li>
            <li>
              <Link to="/contact" style={{ color: "var(--respondit-light)" }}>
                Contact
              </Link>
            </li>
            <li>
              <Link to="/login" style={{ color: "var(--respondit-light)" }}>
                Login
              </Link>
            </li>
            <li>
              <Link to="/signup" style={{ color: "var(--respondit-light)" }}>
                Sign Up
              </Link>
            </li>
          </ul>
        </div>
        <div>
          <h4 style={{ color: "var(--respondit-gold)", marginBottom: "1rem" }}>
            Contact
          </h4>
          <p>
            Email:{" "}
            <a
              href="mailto:support@respondit.com"
              style={{ color: "var(--respondit-gold)" }}
            >
              support@respondit.com
            </a>
          </p>
          <p>LinkedIn | Twitter</p>
        </div>
      </div>
      <div
        style={{
          textAlign: "center",
          marginTop: "2rem",
          fontSize: "0.9rem",
          color: "#ab9883",
          opacity: 0.85,
        }}
      >
        &copy; {new Date().getFullYear()} RESPONDIT. All rights reserved.
      </div>
    </footer>
  );
}

export default Footer;
