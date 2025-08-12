// =========================================
// CodeExamples.js - Show sample API usage code
// Styled with RESPONDIT brand colors & readable code blocks
// =========================================

import React from "react";

function CodeExamples() {
  const jsExample = `
// Submit feedback example
fetch("https://api.respondit.com/feedback", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
    "X-API-Key": "YOUR_API_KEY"
  },
  body: JSON.stringify({
    productId: "12345",
    rating: 5,
    comment: "Great app!"
  })
}).then(res => res.json())
  .then(data => console.log(data));
  `;

  const curlExample = `
curl -X POST "https://api.respondit.com/feedback" \\
  -H "Content-Type: application/json" \\
  -H "X-API-Key: YOUR_API_KEY" \\
  -d '{"productId":"12345","rating":5,"comment":"Great app!"}'
  `;

  return (
    <div className="card" style={{ padding: "2rem" }}>
      <h2 style={{ marginBottom: "1.2rem", color: "var(--respondit-dark)" }}>
        Code Examples
      </h2>

      <div style={{ marginBottom: "2rem" }}>
        <h3 style={{ marginBottom: "0.5rem", color: "var(--respondit-brown)" }}>
          JavaScript (Fetch)
        </h3>
        <pre
          style={{
            backgroundColor: "#f5f2e8",
            padding: "1rem",
            borderRadius: 12,
            fontSize: "0.9rem",
            overflowX: "auto",
            fontFamily: "'Source Code Pro', monospace",
            whiteSpace: "pre-wrap",
            wordBreak: "break-word",
            userSelect: "all",
          }}
        >
          {jsExample.trim()}
        </pre>
      </div>

      <div>
        <h3 style={{ marginBottom: "0.5rem", color: "var(--respondit-brown)" }}>
          cURL
        </h3>
        <pre
          style={{
            backgroundColor: "#f5f2e8",
            padding: "1rem",
            borderRadius: 12,
            fontSize: "0.9rem",
            overflowX: "auto",
            fontFamily: "'Source Code Pro', monospace",
            whiteSpace: "pre-wrap",
            wordBreak: "break-word",
            userSelect: "all",
          }}
        >
          {curlExample.trim()}
        </pre>
      </div>
    </div>
  );
}

export default CodeExamples;
