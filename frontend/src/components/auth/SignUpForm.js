// ====================================
// SignUpForm.js - Handles new user registration
// ====================================

import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { authService } from "../../services/authService"; // Backend signup API

function SignUpForm() {
  // ---------- State for form inputs ----------
  const [name, setName] = useState(""); // Full name
  const [email, setEmail] = useState(""); // Email
  const [password, setPassword] = useState(""); // Password
  const [confirmPassword, setConfirmPassword] = useState(""); // Confirm password

  // UI state
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const navigate = useNavigate();

  // ---------- Handle form submit ----------
  const handleSubmit = async (e) => {
    e.preventDefault(); // stop page refresh
    setError("");

    // Simple client-side validation
    if (!name || !email || !password || !confirmPassword) {
      setError("Please fill in all fields.");
      return;
    }
    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    try {
      setLoading(true);
      // Call backend signup/register API
      const response = await authService.signup(name, email, password);

      // Save API key in localStorage after signup
      localStorage.setItem("respondit_api_key", response.apiKey);

      // Redirect straight to dashboard
      navigate("/dashboard");
    } catch (err) {
      setError(err.message || "Sign up failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form
      className="card"
      onSubmit={handleSubmit}
      style={{ maxWidth: "400px", margin: "0 auto" }}
    >
      {/* ---------- Form Heading ---------- */}
      <h2 style={{ textAlign: "center", marginBottom: "1rem" }}>
        Create Your Account
      </h2>
      <p style={{ textAlign: "center", color: "#666", marginBottom: "1.5rem" }}>
        Join RESPONDIT to start transforming feedback into insights
      </p>

      {/* ---------- Name Input ---------- */}
      <div className="form-group">
        <label className="form-label">Full Name</label>
        <input
          type="text"
          className="form-input"
          placeholder="John Doe"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
      </div>

      {/* ---------- Email Input ---------- */}
      <div className="form-group">
        <label className="form-label">Email</label>
        <input
          type="email"
          className="form-input"
          placeholder="your@email.com"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>

      {/* ---------- Password Input ---------- */}
      <div className="form-group">
        <label className="form-label">Password</label>
        <input
          type="password"
          className="form-input"
          placeholder="••••••••"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>

      {/* ---------- Confirm Password ---------- */}
      <div className="form-group">
        <label className="form-label">Confirm Password</label>
        <input
          type="password"
          className="form-input"
          placeholder="••••••••"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
      </div>

      {/* ---------- Error Message ---------- */}
      {error && (
        <div
          style={{
            background: "#fee2e2",
            padding: "0.75rem",
            borderRadius: "6px",
            color: "#b91c1c",
            marginBottom: "1rem",
          }}
        >
          {error}
        </div>
      )}

      {/* ---------- Submit Button ---------- */}
      <button
        type="submit"
        className="btn btn-primary"
        style={{ width: "100%" }}
        disabled={loading}
      >
        {loading ? "Signing up..." : "Sign Up"}
      </button>
    </form>
  );
}

export default SignUpForm;
