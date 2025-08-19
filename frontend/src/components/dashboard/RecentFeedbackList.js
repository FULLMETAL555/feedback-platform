import React from "react";

// Displays a table of the most recent feedback entries
function RecentFeedbackList({ feedbacks }) {
  if (!feedbacks || feedbacks.length === 0) {
    // Empty state for no feedbacks
    return (
      <div
        style={{
          background: "var(--respondit-white)",
          borderRadius: 18,
          padding: "1.5rem",
          color: "#666",
          textAlign: "center",
          boxShadow: "0 4px 24px rgba(73,51,36,0.07)",
        }}
      >
        No recent feedback found.
      </div>
    );
  }

  // Render table if feedbacks exist
  return (
    <div
      style={{
        background: "var(--respondit-white)",
        borderRadius: 18,
        padding: "1.5rem",
        boxShadow: "0 4px 24px rgba(73,51,36,0.07)",
      }}
    >
      <h3 style={{ marginBottom: "1rem", color: "var(--respondit-brown)" }}>
        Recent Feedback
      </h3>
      <table
        style={{
          width: "100%",
          fontSize: "0.95rem",
          borderCollapse: "collapse",
        }}
      >
        <thead>
          <tr>
            <th>Date</th>
            <th>Product</th>
            <th>Sentiment</th>
            <th>Message</th>
          </tr>
        </thead>
        <tbody>
          {feedbacks.map((fb, idx) => (
            <tr key={idx} style={{ borderBottom: "1px solid #eee" }}>
              {/* Format the date */}
              <td style={{ padding: "0.4rem" }}>
                {new Date(fb.submittedAt).toLocaleDateString()}
              </td>
              <td style={{ padding: "0.4rem" }}>{fb.productName}</td>
              <td style={{ padding: "0.4rem" }}>
                {/* Color-coded sentiment */}
                <span
                  style={{
                    color:
                      fb.sentiment === "Positive"
                        ? "#15803d"
                        : fb.sentiment === "Negative"
                        ? "#b91c1c"
                        : "#636363",
                  }}
                >
                  {fb.sentiment}
                </span>
              </td>
              <td style={{ padding: "0.4rem" }}>{fb.message}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default RecentFeedbackList;
