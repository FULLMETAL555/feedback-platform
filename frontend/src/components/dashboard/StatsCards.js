import React from "react";

function StatsCards({ stats }) {
  // Destructure with defaults
  const {
    totalFeedbacks = 0,
    avgRating = 0,
    numberOfProducts = 0,
    positivePercentage = 0,
  } = stats;

  const statItems = [
    {
      label: "Total Feedbacks",
      value: totalFeedbacks,
      icon: "💬",
    },
    {
      label: "Average Rating",
      value: avgRating.toFixed(2),
      icon: "⭐",
    },
    {
      label: "Products",
      value: numberOfProducts,
      icon: "📦",
    },
    {
      label: "Positive Feedback",
      value: positivePercentage + "%",
      icon: "😊",
    },
  ];

  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))",
        gap: "2rem",
      }}
    >
      {statItems.map(({ label, value, icon }) => (
        <div
          key={label}
          className="card"
          style={{
            textAlign: "center",
            borderRadius: 18,
            boxShadow: "0 4px 24px rgba(73,51,36,0.07)",
            padding: "2rem 1rem",
          }}
        >
          <div style={{ fontSize: "2.5rem", marginBottom: ".5rem" }}>
            {icon}
          </div>
          <div
            style={{
              fontWeight: "600",
              fontSize: "1.6rem",
              color: "var(--respondit-brown)",
            }}
          >
            {value}
          </div>
          <div
            style={{
              marginTop: "0.2rem",
              color: "var(--respondit-dark)",
              fontSize: "1rem",
            }}
          >
            {label}
          </div>
        </div>
      ))}
    </div>
  );
}

export default StatsCards;
