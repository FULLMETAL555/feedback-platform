import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import authService from "../../services/authService";
import { useAuth } from "../../contexts/AuthContext";

function LoginForm() {
  const navigate = useNavigate();
  const { login } = useAuth(); // context method

  const [form, setForm] = useState({ email: "", password: "" });
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
      const { token, user } = await authService.login(
        form.email,
        form.password
      );
      login(token, user); // update context + save to storage
      navigate("/dashboard");
    } catch (err) {
      console.error(err);
      setError("Invalid email or password");
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
            borderRadius: "8px",
            marginBottom: "1rem",
          }}
        >
          {error}
        </div>
      )}
      <input
        name="email"
        type="email"
        placeholder="Email"
        value={form.email}
        onChange={handleChange}
        required
      />
      <input
        name="password"
        type="password"
        placeholder="Password"
        value={form.password}
        onChange={handleChange}
        required
      />
      <button type="submit" disabled={loading}>
        {loading ? "Logging in…" : "Login"}
      </button>
    </form>
  );
}

export default LoginForm;
