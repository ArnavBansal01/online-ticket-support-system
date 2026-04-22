import { analyticsClient } from "./api";

export const analyticsApi = {
  summary: () => analyticsClient.get("/api/analytics/summary"),
  byStatus: () => analyticsClient.get("/api/analytics/by-status"),
  byPriority: () => analyticsClient.get("/api/analytics/by-priority"),
  byCategory: () => analyticsClient.get("/api/analytics/by-category"),
  trend: (period = "30d") =>
    analyticsClient.get("/api/analytics/trend", { params: { period } }),
  agentPerformance: () =>
    analyticsClient.get("/api/analytics/agent-performance"),
  customerStats: (userId) =>
    analyticsClient.get(`/api/analytics/customer-stats/${userId}`),
};
