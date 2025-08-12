import React, { useState } from "react";

function ProductTable({ products = [] }) {
  const [sortKey, setSortKey] = useState("name");
  const [sortAsc, setSortAsc] = useState(true);

  const sortedProducts = [...products].sort((a, b) => {
    const aVal = a[sortKey];
    const bVal = b[sortKey];
    if (typeof aVal === "string") {
      return sortAsc ? aVal.localeCompare(bVal) : bVal.localeCompare(aVal);
    } else {
      return sortAsc ? aVal - bVal : bVal - aVal;
    }
  });

  const headers = [
    { key: "name", label: "Product" },
    { key: "totalFeedback", label: "Total Feedback" },
    { key: "avgRating", label: "Avg Rating" },
    { key: "positivePercentage", label: "Positive %" },
    { key: "lastFeedbackDate", label: "Last Feedback Date" },
  ];

  const onSort = (key) => {
    if (key === sortKey) {
      setSortAsc(!sortAsc);
    } else {
      setSortKey(key);
      setSortAsc(true);
    }
  };

  return (
    <div className="card" style={{ padding: "1rem", borderRadius: 18 }}>
      <table
        style={{
          width: "100%",
          borderCollapse: "collapse",
          fontSize: "0.95rem",
        }}
      >
        <thead>
          <tr>
            {headers.map(({ key, label }) => (
              <th
                key={key}
                onClick={() => onSort(key)}
                style={{
                  cursor: "pointer",
                  borderBottom: "2px solid var(--respondit-brown)",
                  padding: "0.7rem",
                  textAlign: key === "name" ? "left" : "center",
                  color: "var(--respondit-brown)",
                  userSelect: "none",
                }}
                aria-sort={
                  sortKey === key
                    ? sortAsc
                      ? "ascending"
                      : "descending"
                    : "none"
                }
              >
                {label} {sortKey === key ? (sortAsc ? "▲" : "▼") : ""}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {sortedProducts.length === 0 ? (
            <tr>
              <td
                colSpan={headers.length}
                style={{ textAlign: "center", padding: "1rem", color: "#888" }}
              >
                No products found.
              </td>
            </tr>
          ) : (
            sortedProducts.map((p) => (
              <tr
                key={p.productId || p.name}
                style={{ borderBottom: "1px solid #ddd" }}
              >
                <td style={{ textAlign: "left", padding: "0.7rem" }}>
                  {p.name}
                </td>
                <td style={{ textAlign: "center", padding: "0.7rem" }}>
                  {p.totalFeedback}
                </td>
                <td style={{ textAlign: "center", padding: "0.7rem" }}>
                  {p.avgRating?.toFixed(2)}
                </td>
                <td style={{ textAlign: "center", padding: "0.7rem" }}>
                  {p.positivePercentage || 0}%
                </td>
                <td style={{ textAlign: "center", padding: "0.7rem" }}>
                  {new Date(p.lastFeedbackDate).toLocaleDateString()}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default ProductTable;
