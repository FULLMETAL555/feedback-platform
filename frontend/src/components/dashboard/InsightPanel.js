import React from "react";

function InsightPanel({ insights = [] }) {
  if (!insights.length) {
    return (
      <div className="card" style={{ padding: "1.5rem" }}>
        <h3 style={{ color: "var(--respondit-dark)", marginBottom: "1rem" }}>
          AI Insights
        </h3>
        <p style={{ color: "#666" }}>No insights available yet.</p>
      </div>
    );
  }

  // Helper to get background color by average rating (for example)
  const getSentimentColor = (avgRating) => {
    if (avgRating >= 4) return "#d1fae5"; // Light green
    if (avgRating >= 2.5) return "#fef3c7"; // Light yellow
    return "#fee2e2"; // Light red
  };

  return (
    <div
      style={{
        display: "grid",
        gap: "1.8rem",
        gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))",
      }}
    >
      {insights.map(
        ({ categoryName, summary, feedbackCount, averageRating }, idx) => (
          <div
            key={idx}
            className="card"
            style={{
              padding: "1.5rem",
              backgroundColor: getSentimentColor(averageRating),
              borderRadius: 18,
              boxShadow: "0 4px 24px rgba(73,51,36,0.07)",
              color: "var(--respondit-dark)",
            }}
          >
            <h4 style={{ marginBottom: "0.5rem", fontWeight: 600 }}>
              {categoryName}
            </h4>
            <p
              style={{
                fontSize: "1rem",
                lineHeight: 1.4,
                marginBottom: "0.6rem",
              }}
            >
              {summary}
            </p>
            <small>
              Feedback Count: {feedbackCount}
              {averageRating !== undefined
                ? ` | Avg Rating: ${averageRating.toFixed(1)}`
                : ""}
            </small>
          </div>
        )
      )}
    </div>
  );
}

export default InsightPanel;
