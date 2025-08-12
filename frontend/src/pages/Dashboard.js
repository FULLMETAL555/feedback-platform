import React, { useEffect, useState } from "react";
import Header from "../components/common/Header";
import Footer from "../components/common/Footer";
import { feedbackService } from "../services/feedbackService";

import StatsCards from "../components/dashboard/StatsCards";
import InsightPanel from "../components/dashboard/InsightPanel";
import LineChartComponent from "../components/dashboard/LineChartComponent";
import PieChartComponent from "../components/dashboard/PieChartComponent";
import ProductTable from "../components/dashboard/ProductTable";

function Dashboard() {
  const [stats, setStats] = useState(null);
  const [products, setProducts] = useState([]);
  const [insights, setInsights] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [feedbackTrends, setFeedbackTrends] = useState(null);

  useEffect(() => {
    async function loadDashboardData() {
      try {
        setLoading(true);
        setError(null);

        // Parallel fetches
        const [statsRes, productsRes, insightsRes, trendsRes] =
          await Promise.all([
            feedbackService.getRecentStats(),
            feedbackService.getProductSummaries(),
            feedbackService.getInsightSummary(),
            feedbackService.getFeedbackTrends(), // might be null or optional
          ]);

        setStats(statsRes);
        setProducts(productsRes);
        setInsights(insightsRes || []);
        setFeedbackTrends(trendsRes);
      } catch (err) {
        console.error("Dashboard data loading error:", err);
        setError("Failed to load dashboard data. Please try again later.");
      } finally {
        setLoading(false);
      }
    }

    loadDashboardData();
  }, []);

  return (
    <div>
      <Header />
      <main className="container" style={{ padding: "3rem 0" }}>
        <h1
          style={{
            color: "var(--respondit-brown)",
            marginBottom: "2rem",
            fontWeight: 600,
          }}
        >
          Dashboard
        </h1>

        {loading && <p>Loading dashboard data...</p>}
        {error && <p style={{ color: "red" }}>{error}</p>}

        {!loading && !error && stats && (
          <>
            {/* KPI Cards */}
            <StatsCards stats={stats} />

            {/* Charts Section */}
            <section
              style={{
                display: "grid",
                gridTemplateColumns: "repeat(auto-fit, minmax(320px, 1fr))",
                gap: "2.5rem",
                marginTop: "3rem",
              }}
            >
              {/* Feedback Trends (optional) */}
              {feedbackTrends ? (
                <LineChartComponent
                  labels={feedbackTrends.months}
                  feedbackCounts={feedbackTrends.feedbackCounts}
                  averageRatings={feedbackTrends.averageRatings}
                />
              ) : (
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
                  Feedback trends data not available.
                </div>
              )}

              {/* Sentiment Distribution Pie */}
              {insights.length > 0 ? (
                <PieChartComponent
                  labels={insights.map((i) => i.categoryName)}
                  data={insights.map((i) => i.feedbackCount)}
                />
              ) : (
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
                  No sentiment summary available.
                </div>
              )}
            </section>

            {/* AI Insights Panel */}
            <section style={{ marginTop: "3rem" }}>
              <InsightPanel insights={insights} />
            </section>

            {/* Products Table */}
            <section style={{ marginTop: "3rem" }}>
              <ProductTable products={products} />
            </section>
          </>
        )}
      </main>
      <Footer />
    </div>
  );
}

export default Dashboard;
