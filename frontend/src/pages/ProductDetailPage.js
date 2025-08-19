import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Header from "../components/common/Header";
import Footer from "../components/common/Footer";
import FeedbackList from "../components/feedback/FeedbackList";
import ProductInsights from "../components/products/ProductInsights";
import feedbackService from "../services/feedbackService";

function ProductDetailPage() {
  const { productId } = useParams();

  const [insights, setInsights] = useState(null);
  const [feedbacks, setFeedbacks] = useState([]);
  const [loadingInsights, setLoadingInsights] = useState(true);
  const [loadingFeedback, setLoadingFeedback] = useState(true);

  useEffect(() => {
    async function fetchInsights() {
      setLoadingInsights(true);
      try {
        const data = await feedbackService.getProductInsights(productId);
        setInsights(data);
      } catch {
        setInsights(null);
      } finally {
        setLoadingInsights(false);
      }
    }

    async function fetchFeedback() {
      setLoadingFeedback(true);
      try {
        const data = await feedbackService.getProductFeedback(productId);
        setFeedbacks(data);
      } catch {
        setFeedbacks([]);
      } finally {
        setLoadingFeedback(false);
      }
    }

    fetchInsights();
    fetchFeedback();
  }, [productId]);

  return (
    <div className="min-h-screen flex flex-col bg-[#fcf8f4]">
      <Header />

      <main className="flex-1 container mx-auto px-4 py-12 max-w-7xl">
        <h1
          className="mb-6 text-3xl font-semibold"
          style={{ color: "var(--respondit-brown)" }}
        >
          Product Details
        </h1>

        <div className="flex flex-col lg:flex-row gap-8">
          {/* Main Insights Panel */}
          <section className="flex-1 bg-white rounded-xl shadow-md p-6 border border-[#f3eee9]">
            {loadingInsights ? (
              <p className="text-center text-gray-500">Loading insights...</p>
            ) : insights ? (
              <ProductInsights insights={insights} />
            ) : (
              <p className="text-center text-gray-400">
                No insights available.
              </p>
            )}
          </section>

          {/* Feedback Sidebar */}
          <aside className="w-full lg:w-96 bg-white rounded-xl shadow-md p-6 border border-[#f3eee9] overflow-y-auto max-h-[650px]">
            {loadingFeedback ? (
              <p className="text-center text-gray-500">Loading feedback...</p>
            ) : feedbacks.length > 0 ? (
              <FeedbackList feedbacks={feedbacks} />
            ) : (
              <p className="text-center text-gray-400">
                No feedback available.
              </p>
            )}
          </aside>
        </div>
      </main>

      <Footer />
    </div>
  );
}

export default ProductDetailPage;
