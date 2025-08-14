// ==========================
// Header.js - Improved design + hover effects
// ==========================

import React from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";

function Header() {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  // Common link base style
  const baseLink = {
    padding: "0.6rem 1rem",
    borderRadius: "8px",
    textDecoration: "none",
    fontWeight: 500,
    transition: "all 0.2s ease",
  };

  const linkStyle = (path) => ({
    ...baseLink,
    color:
      location.pathname === path
        ? "var(--respondit-gold)"
        : "var(--respondit-brown)",
    fontWeight: location.pathname === path ? "600" : "500",
  });

  const hoverStyle = `
    .nav-link:hover {
      background-color: rgba(163, 148, 106, 0.12);
    }
    .btn-primary:hover {
      background-color: #917f5d;
    }
    .btn-secondary:hover {
      background-color: #e0d8c8;
    }
  `;

  return (
    <>
      {/* Inline styles for hover effects */}
      <style>{hoverStyle}</style>

      <header
        style={{
          background: "var(--respondit-light)",
          padding: "0.9rem 0",
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
            gap: "1rem",
          }}
        >
          {/* Logo */}
          <Link
            to="/"
            className="respondit-logo"
            style={{
              fontSize: "1.6rem",
              fontWeight: "700",
              letterSpacing: "1px",
              color: "var(--respondit-brown)",
              textDecoration: "none",
            }}
          >
            RESPONDIT
          </Link>

          {/* Navigation */}
          <nav style={{ display: "flex", alignItems: "center", gap: "0.6rem" }}>
            {/* Always visible links */}
            <Link
              to="/api-docs"
              style={linkStyle("/api-docs")}
              className="nav-link"
            >
              API Docs
            </Link>
            <Link
              to="/products"
              style={linkStyle("/products")}
              className="nav-link"
            >
              Products
            </Link>
            <Link
              to="/contact"
              style={linkStyle("/contact")}
              className="nav-link"
            >
              Contact
            </Link>

            {/* Logged-in view */}
            {user ? (
              <>
                <Link
                  to="/dashboard"
                  style={linkStyle("/dashboard")}
                  className="nav-link"
                >
                  Dashboard
                </Link>
                <span
                  style={{
                    color: "var(--respondit-brown)",
                    margin: "0 0.6rem 0 0.8rem",
                    fontWeight: 500,
                  }}
                >
                  Hi, {user.name || user.email}
                </span>
                <button
                  onClick={handleLogout}
                  className="btn btn-secondary"
                  style={{
                    padding: "0.45rem 0.9rem",
                    fontSize: "0.9rem",
                    borderRadius: "6px",
                  }}
                >
                  Logout
                </button>
              </>
            ) : (
              <>
                <Link
                  to="/login"
                  className="btn btn-secondary"
                  style={{
                    padding: "0.45rem 0.9rem",
                    fontSize: "0.9rem",
                    borderRadius: "6px",
                  }}
                >
                  Login
                </Link>
                <Link
                  to="/signup"
                  className="btn btn-primary"
                  style={{
                    padding: "0.45rem 1rem",
                    fontSize: "0.9rem",
                    borderRadius: "6px",
                  }}
                >
                  Sign Up
                </Link>
              </>
            )}
          </nav>
        </div>
      </header>
    </>
  );
}

export default Header;
