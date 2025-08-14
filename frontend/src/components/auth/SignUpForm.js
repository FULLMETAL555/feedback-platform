import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import authService from "../../services/authService";
import { useAuth } from "../../contexts/AuthContext";

function SignupForm() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      // Call signup endpoint
      await authService.signup(form.name, form.email, form.password);

      // Optionally, automatically login immediately after signup
      const { token, user } = await authService.login(
        form.email,
        form.password
      );

      // Update auth context and save session
      login(token, user);

      // Redirect to dashboard or another page
      navigate("/dashboard");
    } catch (err) {
      console.error(err);
      setError(err.message || "Sign up failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      {error && (
        <div
          style={{
            background: "#fee2e2",
            padding: "0.5rem",
            color: "#b91c1c",
            borderRadius: 8,
            marginBottom: "1rem",
          }}
        >
          {error}
        </div>
      )}

      <div className="form-group" style={{ marginBottom: "1rem" }}>
        <label>Name</label>
        <input
          name="name"
          type="text"
          placeholder="Your name"
          value={form.name}
          onChange={handleChange}
          required
          style={{ borderRadius: 8 }}
          autoComplete="name"
        />
      </div>

      <div className="form-group" style={{ marginBottom: "1rem" }}>
        <label>Email</label>
        <input
          name="email"
          type="email"
          placeholder="you@example.com"
          value={form.email}
          onChange={handleChange}
          required
          style={{ borderRadius: 8 }}
          autoComplete="email"
        />
      </div>

      <div className="form-group" style={{ marginBottom: "1.5rem" }}>
        <label>Password</label>
        <input
          name="password"
          type="password"
          placeholder="Your password"
          value={form.password}
          onChange={handleChange}
          required
          style={{ borderRadius: 8 }}
          autoComplete="new-password"
        />
      </div>

      <button
        type="submit"
        className="btn btn-primary"
        style={{ width: "100%" }}
        disabled={loading}
        aria-busy={loading}
      >
        {loading ? "Signing up..." : "Sign Up"}
      </button>
    </form>
  );
}

export default SignupForm;
