// src/components/dashboard/BarChartComponent.js
import React from "react";
import {
  BarChart,
  Bar,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";

const data = [
  { name: "Jan", feedbackCount: 40, rating: 4.1 },
  { name: "Feb", feedbackCount: 30, rating: 4.3 },
  { name: "Mar", feedbackCount: 25, rating: 3.9 },
  { name: "Apr", feedbackCount: 50, rating: 4.5 },
  { name: "May", feedbackCount: 20, rating: 4.0 },
];

function BarChartComponent() {
  return (
    <ResponsiveContainer width="100%" height={300}>
      <BarChart
        data={data}
        margin={{ top: 15, right: 30, left: 20, bottom: 5 }}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="name" />
        <YAxis yAxisId="left" orientation="left" />
        <YAxis yAxisId="right" orientation="right" />
        <Tooltip />
        <Legend />
        {/* Feedback count bars */}
        <Bar
          yAxisId="left"
          dataKey="feedbackCount"
          fill="#a3946a"
          name="Feedback Count"
        />
        {/* Average rating line shown as bars here for simplicity */}
        <Bar
          yAxisId="right"
          dataKey="rating"
          fill="#67533b"
          name="Avg. Rating"
        />
      </BarChart>
    </ResponsiveContainer>
  );
}

export default BarChartComponent;
