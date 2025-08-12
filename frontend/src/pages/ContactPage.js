// =========================================
// ContactPage.js - Polished RESPONDIT Contact Page
// Clean, spacious, branded with form & info
// =========================================

import React, { useState } from "react";
import Header from "../components/common/Header";
import Footer from "../components/common/Footer";

function ContactPage() {
  const [form, setForm] = useState({ name: "", email: "", message: "" });
  const [status, setStatus] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setStatus(null);

    // Simulated form submission delay; replace with real API call as needed
    setTimeout(() => {
      setStatus("SUCCESS");
      setForm({ name: "", email: "", message: "" });
    }, 1000);

    // Example real API call placeholder:
    // await contactService.sendMessage(form);
  };

  return (
    <div>
      <Header />

      <main
        className="container"
        style={{ padding: "3rem 0", minHeight: "70vh" }}
      >
        <h1
          style={{
            color: "var(--respondit-brown)",
            marginBottom: "2rem",
            fontWeight: 600,
          }}
        >
          Contact Us
        </h1>

        <p style={{ color: "#666", maxWidth: 600, marginBottom: "2.5rem" }}>
          We'd love to hear from you — whether you're a customer, partner, or
          just curious. Fill out the form and our team will get back to you
          within 24 hours.
        </p>

        <form
          className="card"
          onSubmit={handleSubmit}
          style={{
            maxWidth: 480,
            margin: "0 auto",
            borderRadius: 18,
            boxShadow: "0 4px 24px rgba(73,51,36,0.07)",
            padding: "2rem 1.5rem",
          }}
        >
          <div className="form-group" style={{ marginBottom: "1.25rem" }}>
            <label className="form-label" style={{ fontWeight: 500 }}>
              Name
            </label>
            <input
              name="name"
              type="text"
              className="form-input"
              placeholder="Your name"
              value={form.name}
              onChange={handleChange}
              required
              style={{ borderRadius: 8 }}
            />
          </div>

          <div className="form-group" style={{ marginBottom: "1.25rem" }}>
            <label className="form-label" style={{ fontWeight: 500 }}>
              Email
            </label>
            <input
              name="email"
              type="email"
              className="form-input"
              placeholder="you@email.com"
              value={form.email}
              onChange={handleChange}
              required
              style={{ borderRadius: 8 }}
            />
          </div>

          <div className="form-group" style={{ marginBottom: "1.5rem" }}>
            <label className="form-label" style={{ fontWeight: 500 }}>
              Message
            </label>
            <textarea
              name="message"
              className="form-input"
              placeholder="How can we help?"
              rows={5}
              value={form.message}
              onChange={handleChange}
              required
              style={{ borderRadius: 8 }}
            />
          </div>

          {status === "SUCCESS" && (
            <div
              style={{
                background: "#d1fae5",
                color: "#047857",
                borderRadius: 8,
                padding: "0.7rem",
                marginBottom: "1rem",
                textAlign: "center",
                fontWeight: 600,
              }}
            >
              Message sent!
            </div>
          )}

          <button
            type="submit"
            className="btn btn-primary"
            style={{ width: "100%", fontWeight: 600 }}
          >
            Send Message
          </button>
        </form>

        <div
          style={{
            marginTop: "3.5rem",
            textAlign: "center",
            color: "var(--respondit-brown)",
            fontSize: "1.05rem",
            userSelect: "none",
          }}
        >
          <div>
            <b>Email:</b>{" "}
            <a
              href="mailto:support@respondit.com"
              style={{ color: "var(--respondit-gold)", textDecoration: "none" }}
            >
              support@respondit.com
            </a>
          </div>
          <div style={{ marginTop: "0.7rem" }}>
            <b>LinkedIn:</b> /company/respondit &nbsp;|&nbsp; <b>Twitter:</b>{" "}
            @respondit_ai
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
}

export default ContactPage;
