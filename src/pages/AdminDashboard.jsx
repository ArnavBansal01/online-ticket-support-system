import React, { useCallback, useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import Header from "../components/Header";
import Footer from "../components/Footer";
import LoadingSpinner from "../components/LoadingSpinner";
import TicketStatusBadge from "../components/TicketStatusBadge";
import PriorityBadge from "../components/PriorityBadge";
import { analyticsApi } from "../api/analyticsApi";
import { ticketApi } from "../api/ticketApi";
import { userApi } from "../api/userApi";
import { formatDate } from "../utils/dateUtils";
import { useToast } from "../hooks/useToast";
import { useAuth } from "../hooks/useAuth";
import {
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

const COLORS = ["#2563eb", "#d97706", "#16a34a", "#6b7280"];

export default function AdminDashboard() {
  const [activeTab, setActiveTab] = useState("overview");
  const [summary, setSummary] = useState(null);
  const [byStatus, setByStatus] = useState([]);
  const [byPriority, setByPriority] = useState([]);
  const [agentPerformance, setAgentPerformance] = useState([]);
  const [users, setUsers] = useState([]);
  const [tickets, setTickets] = useState([]);
  const [agents, setAgents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [statusFilter, setStatusFilter] = useState("");
  const [priorityFilter, setPriorityFilter] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("");
  const [agentFilter] = useState("");
  const [dateFrom, setDateFrom] = useState("");
  const [dateTo, setDateTo] = useState("");
  const [selectedIds, setSelectedIds] = useState([]);
  const { showToast } = useToast();
  const { role } = useAuth();

  const loadOverview = useCallback(async () => {
    const [summaryRes, statusRes, priorityRes] = await Promise.all([
      analyticsApi.summary(),
      analyticsApi.byStatus(),
      analyticsApi.byPriority(),
    ]);
    setSummary(summaryRes.data);
    setByStatus(statusRes.data);
    setByPriority(priorityRes.data);
  }, []);

  const loadTickets = useCallback(async () => {
    const { data } = await ticketApi.list({
      status: statusFilter || undefined,
      priority: priorityFilter || undefined,
      category: categoryFilter || undefined,
      assignedTo: agentFilter || undefined,
      dateFrom: dateFrom || undefined,
      dateTo: dateTo || undefined,
      page: 0,
      size: 100,
    });
    setTickets(data.items || []);
  }, [
    statusFilter,
    priorityFilter,
    categoryFilter,
    agentFilter,
    dateFrom,
    dateTo,
  ]);

  const loadUsers = useCallback(async () => {
    const { data } = await userApi.listUsers();
    setUsers(data);
    setAgents(data.filter((user) => user.role === "AGENT"));
  }, []);

  const loadAgents = useCallback(async () => {
    const { data } = await analyticsApi.agentPerformance();
    setAgentPerformance(data);
  }, []);

  const refreshAll = useCallback(async () => {
    setLoading(true);
    try {
      await Promise.all([
        loadOverview(),
        loadTickets(),
        loadUsers(),
        loadAgents(),
      ]);
    } catch (error) {
      showToast(
        error?.response?.data?.error || "Failed to load admin dashboard.",
        "error",
      );
    } finally {
      setLoading(false);
    }
  }, [loadOverview, loadTickets, loadUsers, loadAgents, showToast]);

  useEffect(() => {
    refreshAll();
  }, [refreshAll]);

  useEffect(() => {
    if (activeTab === "tickets") {
      loadTickets().catch((error) =>
        showToast(
          error?.response?.data?.error || "Failed to load tickets.",
          "error",
        ),
      );
    }
  }, [activeTab, loadTickets, showToast]);

  const statusChartData = useMemo(
    () => byStatus.map((row) => ({ name: row.status, value: row.count })),
    [byStatus],
  );
  const priorityChartData = useMemo(
    () => byPriority.map((row) => ({ name: row.priority, value: row.count })),
    [byPriority],
  );

  const assignAgent = async (ticketId, agentId) => {
    try {
      await ticketApi.assign(ticketId, { agentId: Number(agentId) });
      showToast("Ticket assigned successfully.");
      await loadTickets();
    } catch (error) {
      showToast(
        error?.response?.data?.error || "Failed to assign ticket.",
        "error",
      );
    }
  };

  const updateStatus = async (ticketId, status) => {
    try {
      await ticketApi.updateStatus(ticketId, { status });
      showToast("Status updated successfully.");
      await loadTickets();
    } catch (error) {
      showToast(
        error?.response?.data?.error || "Failed to update status.",
        "error",
      );
    }
  };

  const updateRole = async (userId, roleValue) => {
    try {
      await userApi.updateRole(userId, { role: roleValue });
      showToast("Role updated successfully.");
      await loadUsers();
    } catch (error) {
      showToast(
        error?.response?.data?.error || "Failed to update role.",
        "error",
      );
    }
  };

  const deactivateUser = async (userId) => {
    try {
      await userApi.deactivate(userId);
      showToast("User deactivated.");
      await loadUsers();
    } catch (error) {
      showToast(
        error?.response?.data?.error || "Failed to deactivate user.",
        "error",
      );
    }
  };

  const bulkClose = async () => {
    try {
      await Promise.all(
        selectedIds.map((id) =>
          ticketApi.updateStatus(id, { status: "CLOSED" }),
        ),
      );
      setSelectedIds([]);
      showToast("Selected tickets closed.");
      await loadTickets();
    } catch (error) {
      showToast(error?.response?.data?.error || "Bulk update failed.", "error");
    }
  };

  const toggleSelection = (id) => {
    setSelectedIds((current) =>
      current.includes(id)
        ? current.filter((item) => item !== id)
        : [...current, id],
    );
  };

  if (loading) {
    return (
      <>
        <Header />
        <LoadingSpinner />
        <Footer />
      </>
    );
  }

  return (
    <div className="admin-page">
      <Header />
      <div className="admin-layout">
        <aside className="sidebar">
          <h2>ResolveHub Admin</h2>
          <nav>
            <button
              type="button"
              onClick={() => setActiveTab("overview")}
              className={activeTab === "overview" ? "active" : ""}
            >
              Overview
            </button>
            <button
              type="button"
              onClick={() => setActiveTab("tickets")}
              className={activeTab === "tickets" ? "active" : ""}
            >
              All Tickets
            </button>
            <button
              type="button"
              onClick={() => setActiveTab("agents")}
              className={activeTab === "agents" ? "active" : ""}
            >
              Agents
            </button>
            <button
              type="button"
              onClick={() => setActiveTab("users")}
              className={activeTab === "users" ? "active" : ""}
            >
              Users
            </button>
            <button
              type="button"
              onClick={() => setActiveTab("settings")}
              className={activeTab === "settings" ? "active" : ""}
            >
              Settings
            </button>
          </nav>
        </aside>

        <main className="admin-content">
          {activeTab === "overview" && summary && (
            <>
              <section className="admin-header">
                <h2>Welcome, {role || "Admin"}</h2>
                <p style={{ color: "var(--text-light)", marginTop: "4px" }}>
                  Manage and resolve support tickets efficiently.
                </p>
              </section>

              <section className="summary-cards">
                <div className="summary-card">
                  <h3>{summary.totalTickets}</h3>
                  <p>Total Tickets</p>
                </div>
                <div className="summary-card">
                  <h3>{summary.openCount}</h3>
                  <p>Open</p>
                </div>
                <div className="summary-card">
                  <h3>{summary.resolvedCount}</h3>
                  <p>Resolved</p>
                </div>
                <div className="summary-card">
                  <h3>{summary.slaBreachCount}</h3>
                  <p>SLA Breaches</p>
                </div>
              </section>

              <div className="chart-container">
                <h3 style={{ marginBottom: "20px" }}>Tickets by Status</h3>
                <div style={{ width: "100%", height: 320 }}>
                  <ResponsiveContainer width="100%" height="100%">
                    <PieChart>
                      <Pie
                        data={statusChartData}
                        dataKey="value"
                        nameKey="name"
                        outerRadius={110}
                        label
                      >
                        {statusChartData.map((entry, index) => (
                          <Cell
                            key={entry.name}
                            fill={COLORS[index % COLORS.length]}
                          />
                        ))}
                      </Pie>
                      <Tooltip />
                    </PieChart>
                  </ResponsiveContainer>
                </div>
              </div>

              <div className="chart-container">
                <h3 style={{ marginBottom: "20px" }}>Tickets by Priority</h3>
                <div style={{ width: "100%", height: 320 }}>
                  <ResponsiveContainer width="100%" height="100%">
                    <BarChart data={priorityChartData}>
                      <CartesianGrid strokeDasharray="3 3" vertical={false} />
                      <XAxis dataKey="name" />
                      <YAxis />
                      <Tooltip />
                      <Bar dataKey="value" fill="#2563eb" />
                    </BarChart>
                  </ResponsiveContainer>
                </div>
              </div>
            </>
          )}

          {activeTab === "tickets" && (
            <section className="admin-card">
              <div className="flex-between" style={{ marginBottom: "20px" }}>
                <h3 style={{ margin: 0 }}>Ticket Queue</h3>
                <div className="filter-group">
                  <select
                    value={statusFilter}
                    onChange={(e) => setStatusFilter(e.target.value)}
                  >
                    <option value="">Status: All</option>
                    <option value="OPEN">Open</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="RESOLVED">Resolved</option>
                    <option value="CLOSED">Closed</option>
                  </select>
                  <select
                    value={priorityFilter}
                    onChange={(e) => setPriorityFilter(e.target.value)}
                  >
                    <option value="">Priority: All</option>
                    <option value="LOW">Low</option>
                    <option value="MEDIUM">Medium</option>
                    <option value="HIGH">High</option>
                    <option value="URGENT">Urgent</option>
                  </select>
                  <select
                    value={categoryFilter}
                    onChange={(e) => setCategoryFilter(e.target.value)}
                  >
                    <option value="">Category: All</option>
                    <option value="TECHNICAL">Technical</option>
                    <option value="BILLING">Billing</option>
                    <option value="GENERAL">General</option>
                    <option value="FEATURE_REQUEST">Feature Request</option>
                  </select>
                  <input
                    type="date"
                    value={dateFrom}
                    onChange={(e) => setDateFrom(e.target.value)}
                  />
                  <input
                    type="date"
                    value={dateTo}
                    onChange={(e) => setDateTo(e.target.value)}
                  />
                </div>
              </div>

              {selectedIds.length > 0 && (
                <button
                  className="btn-primary"
                  onClick={bulkClose}
                  type="button"
                  style={{ marginBottom: "16px" }}
                >
                  Close Selected ({selectedIds.length})
                </button>
              )}

              <div style={{ overflowX: "auto" }}>
                <table className="admin-table" style={{ width: "100%" }}>
                  <thead>
                    <tr>
                      <th></th>
                      <th>ID</th>
                      <th>Issue Title</th>
                      <th>Priority</th>
                      <th>Status</th>
                      <th>Date</th>
                      <th>Assign Agent</th>
                      <th>Update</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {tickets.map((ticket) => (
                      <tr key={ticket.id}>
                        <td>
                          <input
                            type="checkbox"
                            checked={selectedIds.includes(ticket.id)}
                            onChange={() => toggleSelection(ticket.id)}
                          />
                        </td>
                        <td style={{ fontWeight: 500 }}>{ticket.id}</td>
                        <td>{ticket.title}</td>
                        <td>
                          <PriorityBadge priority={ticket.priority} />
                        </td>
                        <td>
                          <TicketStatusBadge status={ticket.status} />
                        </td>
                        <td>{formatDate(ticket.createdAt)}</td>
                        <td>
                          <select
                            value={ticket.assignedTo || ""}
                            onChange={(e) =>
                              assignAgent(ticket.id, e.target.value)
                            }
                          >
                            <option value="">Unassigned</option>
                            {agents.map((agent) => (
                              <option key={agent.id} value={agent.id}>
                                {agent.name}
                              </option>
                            ))}
                          </select>
                        </td>
                        <td>
                          <select
                            value={ticket.status}
                            onChange={(e) =>
                              updateStatus(ticket.id, e.target.value)
                            }
                          >
                            <option value="OPEN">Open</option>
                            <option value="IN_PROGRESS">In Progress</option>
                            <option value="RESOLVED">Resolved</option>
                            <option value="CLOSED">Closed</option>
                          </select>
                        </td>
                        <td>
                          <Link
                            to={`/tickets/${ticket.id}`}
                            className="view-btn"
                          >
                            Review
                          </Link>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </section>
          )}

          {activeTab === "agents" && (
            <section className="admin-card">
              <h3 style={{ marginBottom: "20px" }}>Agent Performance</h3>
              <div style={{ overflowX: "auto" }}>
                <table style={{ width: "100%" }}>
                  <thead>
                    <tr>
                      <th>Agent Name</th>
                      <th>Tickets Assigned</th>
                      <th>Tickets Resolved</th>
                      <th>Avg Response Time</th>
                    </tr>
                  </thead>
                  <tbody>
                    {agentPerformance.map((agent) => (
                      <tr key={agent.agentId}>
                        <td>{agent.agentName}</td>
                        <td>{agent.ticketsAssigned}</td>
                        <td>{agent.ticketsResolved}</td>
                        <td>{agent.avgResponseTime}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </section>
          )}

          {activeTab === "users" && (
            <section className="admin-card">
              <h3 style={{ marginBottom: "20px" }}>Users</h3>
              <div style={{ overflowX: "auto" }}>
                <table style={{ width: "100%" }}>
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Email</th>
                      <th>Role</th>
                      <th>Join Date</th>
                      <th>Active</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.map((user) => (
                      <tr key={user.id}>
                        <td>{user.name}</td>
                        <td>{user.email}</td>
                        <td>
                          <select
                            value={user.role}
                            onChange={(e) =>
                              updateRole(user.id, e.target.value)
                            }
                          >
                            <option value="CUSTOMER">CUSTOMER</option>
                            <option value="AGENT">AGENT</option>
                            <option value="ADMIN">ADMIN</option>
                          </select>
                        </td>
                        <td>{formatDate(user.createdAt)}</td>
                        <td>{user.active ? "Yes" : "No"}</td>
                        <td>
                          <button
                            className="btn-outline"
                            onClick={() => deactivateUser(user.id)}
                          >
                            Deactivate
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </section>
          )}

          {activeTab === "settings" && (
            <section className="admin-card">
              <h3>Settings</h3>
              <p>This is a placeholder for future admin settings.</p>
            </section>
          )}
        </main>
      </div>
      <Footer />
    </div>
  );
}
