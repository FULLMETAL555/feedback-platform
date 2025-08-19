import React, { useEffect, useState } from "react";
import Header from "../components/common/Header";
import Footer from "../components/common/Footer";
import { feedbackService } from "../services/feedbackService";

import StatsCards from "../components/dashboard/StatsCards";
import InsightPanel from "../components/dashboard/InsightPanel";
import LineChartComponent from "../components/dashboard/LineChartComponent";
import PieChartComponent from "../components/dashboard/PieChartComponent";
import ProductTable from "../components/dashboard/ProductTable";
import RecentFeedbackList from "../components/dashboard/RecentFeedbackList";

function Dashboard() {
  const [kpiStats, setKpiStats] = useState(null);
  const [sentimentDist, setSentimentDist] = useState([]);
  const [categoryInsights, setCategoryInsights] = useState([]);
  const [productPerformance, setProductPerformance] = useState([]);
  const [recentFeedback, setRecentFeedback] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function loadDashboardData() {
      try {
        setLoading(true);
        setError(null);

        const [kpiRes, sentimentRes, insightsRes, productsRes, recentRes] =
          await Promise.all([
            feedbackService.getKpiStats(),
            feedbackService.getSentimentDistribution(),
            feedbackService.getCategoryInsights(),
            feedbackService.getProductPerformance(),
            feedbackService.getRecentFeedback(),
          ]);

        setKpiStats(kpiRes);
        setSentimentDist(sentimentRes || []);
        setCategoryInsights(insightsRes || []);
        setProductPerformance(productsRes || []);
        setRecentFeedback(recentRes || []);
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

        {!loading && !error && kpiStats && (
          <>
            {/* KPI Cards */}
            <StatsCards stats={kpiStats} />

            {/* Charts Section */}
            <section
              style={{
                display: "grid",
                gridTemplateColumns: "repeat(auto-fit, minmax(320px, 1fr))",
                gap: "2.5rem",
                marginTop: "3rem",
              }}
            >
              {/* Feedback Line Chart */}
              <LineChartComponent />

              {/* Sentiment Distribution Pie */}
              {sentimentDist.length > 0 ? (
                <PieChartComponent
                  labels={sentimentDist.map((i) => i.sentiment)}
                  data={sentimentDist.map((i) => i.count)}
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

            {/* AI Insights Panel with scroll */}
            <section
              style={{ marginTop: "3rem" }}
              className="border rounded-lg shadow p-4 bg-white"
            >
              <h2 className="font-semibold text-lg mb-3">AI Insights</h2>
              <div className="overflow-y-auto" style={{ maxHeight: "300px" }}>
                <InsightPanel insights={categoryInsights} />
              </div>
            </section>

            {/* Products Table with scroll */}
            <section
              style={{ marginTop: "3rem" }}
              className="border rounded-lg shadow p-4 bg-white"
            >
              <h2 className="font-semibold text-lg mb-3">
                Product Performance
              </h2>
              <div className="overflow-y-auto" style={{ maxHeight: "300px" }}>
                <ProductTable products={productPerformance} />
              </div>
            </section>

            {/* Recent Feedback List with scroll */}
            <section
              style={{ marginTop: "3rem" }}
              className="border rounded-lg shadow p-4 bg-white"
            >
              <h2 className="font-semibold text-lg mb-3">Recent Feedback</h2>
              <div className="overflow-y-auto" style={{ maxHeight: "300px" }}>
                <RecentFeedbackList feedbacks={recentFeedback} />
              </div>
            </section>
          </>
        )}
      </main>
      <Footer />
    </div>
  );
}

export default Dashboard;
