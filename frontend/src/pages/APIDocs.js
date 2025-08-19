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
          {/* Clarification about API key creation */}
          <div
            style={{
              marginTop: "1rem",
              maxWidth: 650,
              color: "#7a6a4f",
              fontWeight: 500,
            }}
          >
            <p>
              <b>Important:</b> Your unique API key is required to authenticate
              API requests. API keys are issued during the RESPONDIT client
              onboarding process and cannot be created from this page. Please
              obtain your API key from your account dashboard or by contacting
              support.
            </p>
            <p>
              Use the{" "}
              <code
                style={{
                  background: "#ede3b3",
                  padding: "0 .35em",
                  borderRadius: 4,
                }}
              >
                Add API Key
              </code>{" "}
              button above to enter your existing key and quickly test API
              requests.
            </p>
          </div>
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
              <span style={{ letterSpacing: ".5px" }}>
                http://localhost:9099/api/products/addproducts
              </span>{" "}
              – Create a new product
            </li>
            <li>
              <strong>POST</strong>{" "}
              <span style={{ letterSpacing: ".5px" }}>
                http://localhost:9099/api/feedback/submit
              </span>{" "}
              – Submit feedback for a product
            </li>
            {/* <li>
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
            </li> */}
          </ul>
        </section>

        {/* Request Body Examples */}
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
            Request Body Examples
          </h2>

          <div style={{ marginBottom: "1.5rem" }}>
            <h3 style={{ fontWeight: 600, marginBottom: ".5rem" }}>
              POST /products
            </h3>
            <pre
              style={{
                background: "#f9f9f9",
                padding: "1rem",
                borderRadius: 6,
                overflowX: "auto",
              }}
            >
              {`{
  "name": "New Product Name",
  "description": "Detailed description of the product."
}`}
            </pre>
          </div>

          <div>
            <h3 style={{ fontWeight: 600, marginBottom: ".5rem" }}>
              POST /feedback
            </h3>
            <pre
              style={{
                background: "#f9f9f9",
                padding: "1rem",
                borderRadius: 6,
                overflowX: "auto",
              }}
            >
              {`{
    "productId": Id,
   "type": "Feedback Type",
  "message": "Feedback message",
  "submittedBy": "email or username Of user"
  
}`}
            </pre>
          </div>
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
          header to authenticate your requests.
        </div>
      </main>
      <Footer />
    </div>
  );
}

export default APIDocs;
