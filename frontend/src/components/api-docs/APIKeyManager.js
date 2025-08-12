// =========================================
// APIKeyManager.js - Manage API Keys with clean UI
// Styled to match RESPONDIT branding and cards
// =========================================

import React, { useState, useEffect } from "react";
import { apiKeyService } from "../../services/apiKeyService";

function APIKeyManager() {
  const [keys, setKeys] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchKeys() {
      try {
        const data = await apiKeyService.getAPIKeys();
        setKeys(data);
      } catch (err) {
        setError("Failed to load API keys.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    }
    fetchKeys();
  }, []);

  const handleCreateKey = async () => {
    try {
      setLoading(true);
      const newKey = await apiKeyService.createAPIKey();
      setKeys([newKey, ...keys]);
    } catch (err) {
      setError("Failed to create API key.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteKey = async (keyId) => {
    if (!window.confirm("Are you sure you want to delete this key?")) return;
    try {
      setLoading(true);
      await apiKeyService.deleteAPIKey(keyId);
      setKeys(keys.filter((k) => k.id !== keyId));
    } catch (err) {
      setError("Failed to delete API key.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card" style={{ padding: "2rem" }}>
      <h2 style={{ marginBottom: "1rem", color: "var(--respondit-dark)" }}>
        Your API Keys
      </h2>

      {error && (
        <div style={{ color: "red", marginBottom: "1rem" }}>{error}</div>
      )}

      <button
        className="btn btn-primary"
        onClick={handleCreateKey}
        disabled={loading}
        style={{ marginBottom: "1.5rem" }}
      >
        + Create New API Key
      </button>

      {loading && !keys.length ? (
        <p>Loading API keys...</p>
      ) : keys.length === 0 ? (
        <p>No API keys yet. Create one to get started.</p>
      ) : (
        <ul style={{ listStyle: "none", padding: 0, margin: 0 }}>
          {keys.map(({ id, label, key }) => (
            <li
              key={id}
              style={{
                borderBottom: "1px solid #eee",
                padding: "0.75rem 0",
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                fontFamily: "'Courier New', Courier, monospace",
                fontSize: "0.95rem",
                color: "var(--respondit-brown)",
              }}
            >
              <span>{label || "(No Label)"}</span>
              <code style={{ userSelect: "all" }}>{key}</code>
              <button
                className="btn btn-secondary"
                onClick={() => handleDeleteKey(id)}
                style={{ padding: "0.3rem 0.7rem", fontSize: "0.85rem" }}
                disabled={loading}
                aria-label={`Delete API key ${label || id}`}
              >
                Delete
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default APIKeyManager;
