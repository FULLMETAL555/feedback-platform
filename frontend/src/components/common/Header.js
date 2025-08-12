// Header.js

import React from "react";
import { Link } from "react-router-dom";

function Header() {
  return (
    <header
      style={{
        background: "var(--respondit-light)",
        padding: "1.25rem 0",
        boxShadow: "0 2px 6px rgba(73,51,36,0.08)",
        position: "sticky",
        top: 0,
        zIndex: 1000,
      }}
    >
      <div
        className="container"
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <Link to="/" className="respondit-logo" style={{ fontSize: "1.8rem" }}>
          RESPONDIT
        </Link>
        <nav style={{ display: "flex", gap: "2.5rem", fontWeight: 500 }}>
          <Link
            to="/api-docs"
            className="nav-link"
            style={{
              color: "var(--respondit-brown)",
              padding: "0.5rem 1.2rem",
            }}
          >
            API Docs
          </Link>
          <Link
            to="/products"
            className="nav-link"
            style={{
              color: "var(--respondit-brown)",
              padding: "0.5rem 1.2rem",
            }}
          >
            Products
          </Link>
          <Link
            to="/contact"
            className="nav-link"
            style={{
              color: "var(--respondit-brown)",
              padding: "0.5rem 1.2rem",
            }}
          >
            Contact
          </Link>
          <Link
            to="/login"
            className="btn btn-secondary"
            style={{ padding: "0.5rem 1rem" }}
          >
            Login
          </Link>
          <Link
            to="/signup"
            className="btn btn-primary"
            style={{ padding: "0.5rem 1.25rem" }}
          >
            Sign Up
          </Link>
        </nav>
      </div>
    </header>
  );
}

export default Header;
