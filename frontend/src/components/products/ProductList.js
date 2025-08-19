import React, { useState } from "react";

function ProductList({ products = [], onAddClick, onSelectProduct }) {
  const [search, setSearch] = useState("");

  // Filter products by name or description
  const filtered = products.filter(
    (prod) =>
      prod.name.toLowerCase().includes(search.toLowerCase()) ||
      (prod.description &&
        prod.description.toLowerCase().includes(search.toLowerCase()))
  );

  // Your RESPONDIT theme colors
  const BORDER_COLOR = "#f3eee9";
  const BOX_SHADOW = "rgba(73, 51, 36, 0.07)";
  const TEXT_PRIMARY = "#493324";
  const TEXT_SECONDARY = "#ab9876";
  const BUTTON_BG = "#c2965aff";
  const BUTTON_HOVER_BG = "#bb7f23";

  return (
    <div>
      {/* Search and Add Button */}
      <div className="flex flex-col sm:flex-row items-stretch sm:items-center justify-between mb-6 gap-3">
        <input
          type="text"
          placeholder="Search products..."
          className="flex-1 rounded-lg px-3 py-2 focus:outline-none transition"
          style={{
            border: `1px solid ${BORDER_COLOR}`,
            backgroundColor: "#fff",
            color: TEXT_PRIMARY,
          }}
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          onFocus={(e) =>
            (e.target.style.boxShadow = `0 0 0 3px ${BUTTON_BG}66`)
          }
          onBlur={(e) => (e.target.style.boxShadow = "none")}
        />
        {onAddClick && (
          <button
            onClick={onAddClick}
            className="rounded-lg px-4 py-2 font-medium transition text-white"
            style={{ backgroundColor: BUTTON_BG }}
            onMouseEnter={(e) =>
              (e.currentTarget.style.backgroundColor = BUTTON_HOVER_BG)
            }
            onMouseLeave={(e) =>
              (e.currentTarget.style.backgroundColor = BUTTON_BG)
            }
          >
            + Add Product
          </button>
        )}
      </div>

      {/* Product List */}
      <ul>
        {filtered.length === 0 && (
          <li
            className="text-center py-8"
            style={{ color: TEXT_SECONDARY, fontWeight: 500 }}
          >
            No products found.
          </li>
        )}
        {filtered.map((product) => (
          <li
            key={product.id}
            onClick={() => onSelectProduct && onSelectProduct(product)}
            className="rounded-xl cursor-pointer transition"
            style={{
              backgroundColor: "#fff",
              border: `1px solid ${BORDER_COLOR}`,
              boxShadow: `0 2px 8px 0 ${BOX_SHADOW}`,
              padding: "1rem",
              marginBottom: "1rem",
              color: TEXT_PRIMARY,
            }}
            onMouseEnter={(e) =>
              (e.currentTarget.style.boxShadow = `0 4px 24px 0 ${BOX_SHADOW}`)
            }
            onMouseLeave={(e) =>
              (e.currentTarget.style.boxShadow = `0 2px 8px 0 ${BOX_SHADOW}`)
            }
          >
            <div className="flex justify-between items-center">
              <span
                className="font-semibold"
                style={{ fontSize: "1.08rem", color: TEXT_PRIMARY }}
              >
                {product.name}
              </span>
              {/* If you want to display clientId, uncomment below */}
              {/* <span className="text-xs ml-4" style={{ color: TEXT_SECONDARY }}>
                {product.clientId}
              </span> */}
            </div>
            {product.description && (
              <p className="mt-1 text-sm" style={{ color: "#81634b" }}>
                {product.description}
              </p>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ProductList;
