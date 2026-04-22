import React from "react";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

export default function TrendChart({ data = [] }) {
  const chartData = data.map((point) => ({
    name: point.date?.slice(5) || point.name,
    tickets: point.count ?? point.tickets ?? 0,
  }));

  return (
    <div className="chart-container">
      <h3 style={{ marginBottom: "20px", color: "var(--text-dark)" }}>
        Ticket Trends
      </h3>
      <div style={{ width: "100%", height: 300 }}>
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart
            data={chartData}
            margin={{ top: 10, right: 30, left: 0, bottom: 0 }}
          >
            <defs>
              <linearGradient id="colorTickets" x1="0" y1="0" x2="0" y2="1">
                <stop
                  offset="5%"
                  stopColor="var(--primary)"
                  stopOpacity={0.3}
                />
                <stop offset="95%" stopColor="var(--primary)" stopOpacity={0} />
              </linearGradient>
            </defs>
            <CartesianGrid
              strokeDasharray="3 3"
              vertical={false}
              stroke="var(--border-light)"
            />
            <XAxis
              dataKey="name"
              axisLine={false}
              tickLine={false}
              tick={{ fill: "var(--text-light)", fontSize: 12 }}
              dy={10}
            />
            <YAxis
              axisLine={false}
              tickLine={false}
              tick={{ fill: "var(--text-light)", fontSize: 12 }}
            />
            <Tooltip
              contentStyle={{
                borderRadius: "8px",
                border: "none",
                boxShadow: "var(--shadow-md)",
              }}
              itemStyle={{ color: "var(--primary)", fontWeight: 500 }}
            />
            <Area
              type="monotone"
              dataKey="tickets"
              stroke="var(--primary)"
              strokeWidth={3}
              fillOpacity={1}
              fill="url(#colorTickets)"
            />
          </AreaChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
