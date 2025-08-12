// src/components/dashboard/PieChartComponent.js
import React from "react";
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";

const data = [
  { name: "Feature A", value: 400 },
  { name: "Feature B", value: 300 },
  { name: "Feature C", value: 300 },
  { name: "Feature D", value: 200 },
];

const COLORS = ["#a3946a", "#67533b", "#493324", "#f5f2e8"];

function PieChartComponent() {
  return (
    <ResponsiveContainer width="100%" height={300}>
      <PieChart>
        <Pie
          dataKey="value"
          isAnimationActive={true}
          data={data}
          cx="50%"
          cy="50%"
          outerRadius={100}
          label
        >
          {data.map((entry, index) => (
            <Cell key={`slice-${index}`} fill={COLORS[index % COLORS.length]} />
          ))}
        </Pie>
        <Tooltip />
        <Legend />
      </PieChart>
    </ResponsiveContainer>
  );
}

export default PieChartComponent;
