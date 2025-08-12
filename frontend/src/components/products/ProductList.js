// =============================================
// ProductList.js - Clean product list card
// =============================================

import React, { useEffect, useState } from "react";
import { productService } from "../../services/productService";

function ProductList({ onAddClick }) {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    async function loadProducts() {
      try {
        const data = await productService.getProducts();
        setProducts(data);
      } catch (err) {
        console.error("Error loading products", err);
      }
    }
    loadProducts();
  }, []);

  return (
    <div className="card" style={{ paddingBottom: "1rem" }}>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: "1.5rem",
        }}
      >
        <h3 style={{ margin: 0, color: "var(--respondit-dark)" }}>
          Your Products
        </h3>
        <button
          className="btn btn-primary"
          style={{ padding: "0.5rem 1.2rem" }}
          onClick={onAddClick}
        >
          ➕ New Product
        </button>
      </div>
      {products.length === 0 ? (
        <p style={{ color: "#888", textAlign: "center", margin: "1rem 0" }}>
          No products yet. Add one to get started.
        </p>
      ) : (
        <ul
          style={{
            listStyle: "none",
            padding: 0,
            margin: 0,
          }}
        >
          {products.map((p) => (
            <li
              key={p.id}
              style={{
                borderBottom: "1px solid #eee",
                padding: "0.75rem 0",
                marginBottom: "0.7rem",
              }}
            >
              <div style={{ fontWeight: 600, color: "var(--respondit-brown)" }}>
                {p.name}
              </div>
              <div style={{ color: "#555", fontSize: "0.97rem" }}>
                {p.description}
              </div>
              <small style={{ color: "#a3946a" }}>Product ID: {p.id}</small>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default ProductList;
