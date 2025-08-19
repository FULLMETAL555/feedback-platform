import React, { useState, useEffect } from "react";

function APIKeyManager() {
  const [keys, setKeys] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchKeys = () => {
    setLoading(true);
    setError(null);
    try {
      const data = localStorage.getItem("respondit_api_key");
      if (data) {
        const parsedKeys = Array.isArray(data) ? data : [data];
        setKeys(parsedKeys);
      } else {
        setKeys([]);
      }
    } catch (err) {
      setError("Failed to load API keys.");
      console.error(err);
      setKeys([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchKeys();
  }, []);

  return (
    <div
      className="card"
      style={{
        padding: "2rem",
        borderRadius: 18,
        boxShadow: "0 4px 24px rgba(73,51,36,0.07)",
        backgroundColor: "#fff",
        maxWidth: 600,
        margin: "0 auto",
      }}
    >
      <h2
        style={{
          marginBottom: "1.5rem",
          color: "var(--respondit-dark)",
          fontWeight: 600,
          fontSize: "1.5rem",
          textAlign: "center",
          fontFamily: "'Nunito Sans', sans-serif",
        }}
      >
        Your API Keys
      </h2>

      {error && (
        <div
          style={{
            color: "#b00020",
            backgroundColor: "#fcebea",
            padding: "1rem",
            borderRadius: 8,
            marginBottom: "1.5rem",
            fontWeight: 500,
          }}
          role="alert"
        >
          {error}
        </div>
      )}

      <button
        className="btn btn-primary"
        onClick={fetchKeys}
        disabled={loading}
        style={{
          display: "block",
          margin: "0 auto 1.5rem auto",
          padding: "0.75rem 1.5rem",
          backgroundColor: "var(--respondit-amber, #dd9227)",
          color: "#fff",
          border: "none",
          borderRadius: 10,
          fontWeight: 600,
          fontSize: "1rem",
          cursor: loading ? "not-allowed" : "pointer",
          boxShadow: "0 4px 12px rgba(221,146,39,0.4)",
          transition: "background-color 0.3s ease",
        }}
        onMouseEnter={(e) =>
          (e.currentTarget.style.backgroundColor =
            "var(--respondit-amber-dark, #8d7a5dff)")
        }
        onMouseLeave={(e) =>
          (e.currentTarget.style.backgroundColor =
            "var(--respondit-amber, #a19480ff)")
        }
        aria-label="Refresh API keys"
      >
        {loading ? "Refreshing..." : "Refresh API Key"}
      </button>

      {loading && !keys.length ? (
        <p
          style={{
            textAlign: "center",
            color: "#8c7b5a",
            fontWeight: 500,
            fontFamily: "'Nunito Sans', sans-serif",
          }}
          aria-live="polite"
        >
          Loading API keys...
        </p>
      ) : keys.length === 0 ? (
        <p
          style={{
            textAlign: "center",
            color: "#ab9876",
            fontWeight: 500,
            fontFamily: "'Nunito Sans', sans-serif",
          }}
          aria-live="polite"
        >
          No API keys available. Please obtain your key from your account
          dashboard.
        </p>
      ) : (
        <ul
          style={{
            listStyle: "none",
            padding: 0,
            margin: 0,
            fontFamily: "'Courier New', Courier, monospace",
            fontSize: "1rem",
            color: "var(--respondit-brown)",
            maxHeight: 200,
            overflowY: "auto",
            borderTop: "1px solid #eee",
            borderBottom: "1px solid #eee",
            borderRadius: 10,
          }}
          aria-label="List of API keys"
        >
          {keys.map((key, index) => (
            <li
              key={index}
              style={{
                padding: "0.75rem 1rem",
                borderBottom:
                  index !== keys.length - 1 ? "1px solid #eee" : "none",
                userSelect: "all",
                wordBreak: "break-all",
              }}
            >
              {typeof key === "string" ? key : key.key}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default APIKeyManager;
