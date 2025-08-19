// Products.js
import React, { useEffect, useState } from "react";
import Header from "../components/common/Header";
import Footer from "../components/common/Footer";
import ProductList from "../components/products/ProductList";
import CreateProduct from "../components/products/CreateProduct";
import { productService } from "../services/productService";

function Products() {
  const [showForm, setShowForm] = useState(false);
  const [reloadFlag, setReloadFlag] = useState(false);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  //fetch products
  useEffect(() => {
    let isCurrent = true;

    async function fetchProducts() {
      setLoading(true);
      setError(null);
      try {
        const data = await productService.getProducts();
        if (isCurrent) setProducts(data);
      } catch (err) {
        if (isCurrent) {
          setError(err.message || "Failed to fetch products.");
          setProducts([]);
        }
      } finally {
        if (isCurrent) setLoading(false);
      }
    }

    fetchProducts();

    return () => {
      isCurrent = false;
    };
  }, [reloadFlag]);
  // Called when a new product is created
  const handleProductCreated = () => {
    setShowForm(false);
    setReloadFlag(!reloadFlag); // trigger reload
  };

  return (
    <div className="min-h-screen flex flex-col bg-[#fcf8f4]">
      <Header />

      <main className="flex-1 px-4 py-12 container mx-auto max-w-3xl">
        <h1
          className="mb-8 text-3xl font-semibold"
          style={{ color: "var(--respondit-brown)" }}
        >
          Manage Products
        </h1>
        <p className="text-[#ab9876] text-base mb-8 max-w-lg">
          Here you can create new products, view the list, and get ready to
          collect feedback through the API.
        </p>

        <div
          className="bg-white rounded-xl shadow-md p-6"
          style={{
            border: "1px solid #f3eee9",
            boxShadow: "0 4px 24px rgba(73,51,36,0.07)",
          }}
        >
          {loading && (
            <p className="text-center text-gray-500">Loading products...</p>
          )}
          {error && <p className="text-center text-red-600">Error: {error}</p>}
          {!loading && !error && (
            <ProductList
              products={products}
              onAddClick={() => setShowForm(true)}
            />
          )}

          {showForm && (
            <div className="mt-6 border-t border-[#f3eee9] pt-6">
              <CreateProduct onProductCreated={handleProductCreated} />
            </div>
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
}

export default Products;
