// =============================================
// productService.js - Backend API calls for products
// =============================================

import api from "./api";

export const productService = {
  getProducts: async () => {
    const res = await api.get("/products");
    return res.data;
  },
  createProduct: async (productData) => {
    const res = await api.post("/products", productData);
    return res.data;
  },
  deleteProduct: async (id) => {
    const res = await api.delete(`/products/${id}`);
    return res.data;
  },
};
