// =============================================
// CreateProduct.js - Form to add a new product
// =============================================

import React, { useState } from "react";
import { productService } from "../../services/productService";

function CreateProduct({ onProductCreated }) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [category, setCategory] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!name || !description) {
      alert("Name and description are required");
      return;
    }
    try {
      await productService.createProduct({ name, description, category });
      alert("Product created successfully");
      onProductCreated();
    } catch (err) {
      console.error(err);
      alert("Error creating product");
    }
  };

  return (
    <div className="card" style={{ marginTop: "1rem" }}>
      <h3>Create New Product</h3>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label className="form-label">Product Name</label>
          <input
            type="text"
            className="form-input"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Enter product name"
          />
        </div>
        <div className="form-group">
          <label className="form-label">Description</label>
          <textarea
            className="form-input"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Enter product description"
          ></textarea>
        </div>
        <div className="form-group">
          <label className="form-label">Category (optional)</label>
          <input
            type="text"
            className="form-input"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            placeholder="Enter category"
          />
        </div>
        <button type="submit" className="btn btn-primary">
          Add Product
        </button>
      </form>
    </div>
  );
}

export default CreateProduct;
