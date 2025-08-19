import React, { useState, useEffect } from "react";
import {
  LineChart,
  Line,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";
import feedbackService from "../../services/feedbackService";
/**
 * LineChartComponent
 * Shows aggregated feedback trends for the authenticated client (determined by API key).
 * No clientId prop needed.
 */
function LineChartComponent() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let isCurrent = true;

    setLoading(true);
    setError(null);

    async function fetchTrends() {
      try {
        const fetchedData = await feedbackService.getClientFeedbackTrends();
        if (isCurrent) setData(fetchedData);
      } catch (err) {
        if (isCurrent) setError(err.message || "Failed to fetch data");
      } finally {
        if (isCurrent) setLoading(false);
      }
    }

    fetchTrends();

    return () => {
      isCurrent = false;
    };
  }, []);

  if (loading)
    return <div className="text-center p-4">Loading chart data...</div>;
  if (error)
    return <div className="text-center text-red-600 p-4">Error: {error}</div>;
  if (!data.length)
    return <div className="text-center p-4">No data available</div>;

  return (
    <div className="bg-white border rounded-lg shadow p-4">
      <h2 className="font-semibold text-xl mb-4">
        Feedback & Ratings Over Time
      </h2>
      <ResponsiveContainer width="100%" height={300}>
        <LineChart
          data={data}
          margin={{ top: 15, right: 30, left: 20, bottom: 5 }}
        >
          <CartesianGrid stroke="#ccc" />
          <XAxis dataKey="period" />
          <YAxis
            yAxisId="left"
            label={{
              value: "Feedback Count",
              angle: -90,
              position: "insideLeft",
            }}
          />
          <YAxis
            yAxisId="right"
            orientation="right"
            domain={[0, 5]}
            label={{
              value: "Average Rating",
              angle: 90,
              position: "insideRight",
            }}
          />
          <Tooltip />
          <Legend verticalAlign="top" height={36} />
          <Line
            yAxisId="left"
            type="monotone"
            dataKey="feedbackCount"
            stroke="#a3946a"
            name="Feedback Count"
            strokeWidth={2}
          />
          <Line
            yAxisId="right"
            type="monotone"
            dataKey="avgRating"
            stroke="#67533b"
            name="Avg. Rating"
            strokeWidth={2}
            dot={{ r: 4 }}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}

export default LineChartComponent;
