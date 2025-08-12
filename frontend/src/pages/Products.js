// =============================================
// Products.js - Manage products with modern layout
// Polished with RESPONDIT brand colors & spacing
// =============================================

import React, { useState } from "react";
import Header from "../components/common/Header";
import Footer from "../components/common/Footer";
import ProductList from "../components/products/ProductList";
import CreateProduct from "../components/products/CreateProduct";

function Products() {
  const [showForm, setShowForm] = useState(false);
  const [reloadFlag, setReloadFlag] = useState(false);

  // Called when a new product is created
  const handleProductCreated = () => {
    setShowForm(false);
    setReloadFlag(!reloadFlag); // refresh ProductList
  };

  return (
    <div>
      <Header />

      <main className="container" style={{ padding: "3rem 0" }}>
        <h1
          style={{
            marginBottom: "2rem",
            color: "var(--respondit-brown)",
            fontWeight: 600,
          }}
        >
          Manage Products
        </h1>
        <p
          style={{
            color: "#666",
            fontSize: "1.08rem",
            maxWidth: 630,
            marginBottom: "2.8rem",
          }}
        >
          Here you can create new products, view the list, and get ready to
          collect feedback through the API.
        </p>

        <div style={{ maxWidth: 600, margin: "0 auto" }}>
          <ProductList onAddClick={() => setShowForm(true)} key={reloadFlag} />
          {showForm && (
            <CreateProduct onProductCreated={handleProductCreated} />
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
}

export default Products;
