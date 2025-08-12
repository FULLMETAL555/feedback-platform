// ==============================================
// APIDocs.js - RESPONDIT Polished API Docs Page
// Clean, modern UI with branding & structure
// ==============================================

import React from "react";
import Header from "../components/common/Header";
import Footer from "../components/common/Footer";
import APIKeyManager from "../components/api-docs/APIKeyManager";
import CodeExamples from "../components/api-docs/CodeExamples";

function APIDocs() {
  return (
    <div>
      <Header />
      <main className="container" style={{ padding: "3rem 0" }}>
        {/* Page Title */}
        <h1
          style={{
            color: "var(--respondit-brown)",
            marginBottom: "1.5rem",
            fontWeight: 600,
          }}
        >
          API Documentation
        </h1>
        <p
          style={{
            color: "#666",
            fontSize: "1.1rem",
            maxWidth: 650,
            marginBottom: "2.5rem",
          }}
        >
          Use your unique API key to securely access RESPONDIT endpoints. See
          sample requests in multiple languages below.
        </p>

        {/* API Key Manager */}
        <section style={{ marginBottom: "2rem" }}>
          <APIKeyManager />
        </section>

        {/* Endpoints List in Card */}
        <section
          className="card"
          style={{
            marginBottom: "2rem",
            padding: "2rem",
            borderRadius: 18,
            boxShadow: "0 4px 24px rgba(73,51,36,0.07)",
          }}
        >
          <h2
            style={{
              color: "var(--respondit-dark)",
              marginBottom: "1.5rem",
              fontSize: "1.35rem",
              fontWeight: 600,
            }}
          >
            Available Endpoints
          </h2>
          <ul style={{ lineHeight: 2, fontSize: "1.05rem", color: "#493324" }}>
            <li>
              <strong>POST</strong>{" "}
              <span style={{ letterSpacing: ".5px" }}>/products</span> – Create
              a new product
            </li>
            <li>
              <strong>POST</strong>{" "}
              <span style={{ letterSpacing: ".5px" }}>/feedback</span> – Submit
              feedback for a product
            </li>
            <li>
              <strong>GET</strong>{" "}
              <span style={{ letterSpacing: ".5px" }}>
                /products/&#123;id&#125;/feedback
              </span>{" "}
              – List feedback for a product
            </li>
            <li>
              <strong>GET</strong>{" "}
              <span style={{ letterSpacing: ".5px" }}>
                /products/&#123;id&#125;/insights
              </span>{" "}
              – Get AI insights for a product
            </li>
          </ul>
        </section>

        {/* Code Examples */}
        <section style={{ marginBottom: "2rem" }}>
          <CodeExamples />
        </section>

        {/* Gold-Toned Informational Tip Card */}
        <div
          className="card"
          style={{
            background: "#fff8e1",
            borderLeft: "5px solid var(--respondit-gold)",
            color: "#493324",
            padding: "1.15rem 1.5rem",
            marginBottom: "2rem",
            fontWeight: 500,
          }}
        >
          <b>Tip:</b> Always include your API key in the{" "}
          <code
            style={{
              background: "#ede3b3",
              borderRadius: 4,
              padding: "0 .35em",
            }}
          >
            X-API-Key
          </code>{" "}
          header.
        </div>
      </main>
      <Footer />
    </div>
  );
}

export default APIDocs;
