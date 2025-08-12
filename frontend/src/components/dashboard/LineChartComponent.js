// src/components/dashboard/LineChartComponent.js
import React from "react";
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

const data = [
  { name: "Jan", feedbackCount: 40, rating: 4.1 },
  { name: "Feb", feedbackCount: 30, rating: 4.3 },
  { name: "Mar", feedbackCount: 25, rating: 3.9 },
  { name: "Apr", feedbackCount: 50, rating: 4.5 },
  { name: "May", feedbackCount: 20, rating: 4.0 },
];

function LineChartComponent() {
  return (
    <ResponsiveContainer width="100%" height={300}>
      <LineChart
        data={data}
        margin={{ top: 15, right: 30, left: 20, bottom: 5 }}
      >
        <CartesianGrid stroke="#ccc" />
        <XAxis dataKey="name" />
        <YAxis />
        <Tooltip />
        <Legend />
        {/* Line for feedback count */}
        <Line
          type="monotone"
          dataKey="feedbackCount"
          stroke="#a3946a"
          name="Feedback Count"
        />
        {/* Line for average rating */}
        <Line
          type="monotone"
          dataKey="rating"
          stroke="#67533b"
          name="Avg. Rating"
        />
      </LineChart>
    </ResponsiveContainer>
  );
}

export default LineChartComponent;
