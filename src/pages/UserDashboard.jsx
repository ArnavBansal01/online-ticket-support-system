import React, { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import TrendChart from "../components/TrendChart";
import TicketModal from "../components/TicketModal";
import Header from "../components/Header";
import Footer from "../components/Footer";
import { analyticsApi } from "../api/analyticsApi";
import { useTickets } from "../hooks/useTickets";
import { useAuth } from "../hooks/useAuth";
import LoadingSpinner from "../components/LoadingSpinner";
import TicketStatusBadge from "../components/TicketStatusBadge";
import PriorityBadge from "../components/PriorityBadge";
import { formatDate } from "../utils/dateUtils";
import { toMinutesLabel } from "../utils/formatters";
import { useToast } from "../hooks/useToast";

export default function UserDashboard() {
  const [isModalOpen, setModalOpen] = useState(false);
  const [status, setStatus] = useState("");
  const [priority, setPriority] = useState("");
  const [dateFrom, setDateFrom] = useState("");
  const [dateTo, setDateTo] = useState("");
  const [summary, setSummary] = useState({
    openTickets: 0,
    resolvedThisMonth: 0,
    avgResponseTime: 0,
  });
  const [trend, setTrend] = useState([]);
  const { user } = useAuth();
  const { tickets, loading, fetchTickets } = useTickets();
  const { showToast } = useToast();
  const ticketQuery = useMemo(
    () => ({
      status: status || undefined,
      priority: priority || undefined,
      dateFrom: dateFrom || undefined,
      dateTo: dateTo || undefined,
      page: 0,
      size: 100,
      ...(user?.role === "AGENT" ? { assignedTo: user.id } : {}),
    }),
    [status, priority, dateFrom, dateTo, user],
  );

  useEffect(() => {
    fetchTickets(ticketQuery).catch((error) => {
      showToast(
        error?.response?.data?.error || "Failed to load tickets.",
        "error",
      );
    });
  }, [ticketQuery, fetchTickets, showToast]);

  useEffect(() => {
    let active = true;

    if (!user?.id) {
      return () => {
        active = false;
      };
    }

    Promise.all([
      analyticsApi.customerStats(user.id),
      analyticsApi.trend("30d"),
    ])
      .then(([summaryRes, trendRes]) => {
        if (!active) {
          return;
        }
        setSummary(summaryRes.data);
        setTrend(trendRes.data);
      })
      .catch((error) => {
        if (!active) {
          return;
        }
        showToast(
          error?.response?.data?.error || "Failed to load dashboard data.",
          "error",
        );
      });

    return () => {
      active = false;
    };
  }, [user?.id, showToast]);

  return (
    <>
      <Header />
      <div className="user-page">
        <main className="user-container">
          <div className="flex-between user-header">
            <div>
              <h2>Welcome back, {user?.name || "User"}</h2>
              <p style={{ color: "var(--text-light)", marginTop: "4px" }}>
                Track and manage your support tickets efficiently.
              </p>
            </div>
            <button onClick={() => setModalOpen(true)} className="btn-primary">
              + Create Ticket
            </button>
          </div>

          <section className="summary-cards">
            <div className="summary-card">
              <h3>{summary.openTickets}</h3>
              <p>My Open Tickets</p>
            </div>
            <div className="summary-card">
              <h3 style={{ color: "#16a34a" }}>{summary.resolvedThisMonth}</h3>
              <p>Resolved This Month</p>
            </div>
            <div className="summary-card">
              <h3 style={{ color: "#d97706" }}>
                {toMinutesLabel(summary.avgResponseTime)}
              </h3>
              <p>Avg Response Time</p>
            </div>
          </section>

          <TrendChart data={trend} />

          <section className="user-card" style={{ marginTop: "40px" }}>
            <div className="filter-bar">
              <h3 style={{ margin: 0 }}>Recent Tickets</h3>
              <div className="filter-group">
                <select
                  value={status}
                  onChange={(e) => setStatus(e.target.value)}
                >
                  <option value="">All Status</option>
                  <option value="OPEN">Open</option>
                  <option value="IN_PROGRESS">In Progress</option>
                  <option value="RESOLVED">Resolved</option>
                  <option value="CLOSED">Closed</option>
                </select>
                <select
                  value={priority}
                  onChange={(e) => setPriority(e.target.value)}
                >
                  <option value="">All Priority</option>
                  <option value="LOW">Low</option>
                  <option value="MEDIUM">Medium</option>
                  <option value="HIGH">High</option>
                  <option value="URGENT">Urgent</option>
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

            {loading && <LoadingSpinner />}
            <div style={{ overflowX: "auto" }}>
              <table className="user-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Issue Title</th>
                    <th>Category</th>
                    <th>Priority</th>
                    <th>Status</th>
                    <th>Date</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {tickets.length > 0 ? (
                    tickets.map((ticket) => (
                      <tr key={ticket.id}>
                        <td style={{ fontWeight: 500 }}>{ticket.id}</td>
                        <td>{ticket.title}</td>
                        <td>{ticket.category}</td>
                        <td>
                          <PriorityBadge priority={ticket.priority} />
                        </td>
                        <td>
                          <TicketStatusBadge status={ticket.status} />
                        </td>
                        <td>{formatDate(ticket.createdAt)}</td>
                        <td>
                          <Link
                            to={`/tickets/${ticket.id}`}
                            className="view-btn"
                          >
                            View Details
                          </Link>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td
                        colSpan="7"
                        style={{
                          textAlign: "center",
                          padding: "30px",
                          color: "var(--text-light)",
                        }}
                      >
                        No tickets match the selected filter.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </section>
        </main>

        <TicketModal
          isOpen={isModalOpen}
          onClose={() => setModalOpen(false)}
          onCreated={(ticket) => {
            fetchTickets(ticketQuery);
            window.location.href = `/tickets/${ticket.id}`;
          }}
        />
      </div>
      <Footer />
    </>
  );
}
