import React, { useCallback, useEffect, useMemo, useState } from "react";
import Header from "../components/Header";
import Footer from "../components/Footer";
import { useNavigate, useParams } from "react-router-dom";
import { ticketApi } from "../api/ticketApi";
import LoadingSpinner from "../components/LoadingSpinner";
import TicketStatusBadge from "../components/TicketStatusBadge";
import PriorityBadge from "../components/PriorityBadge";
import { useAuth } from "../hooks/useAuth";
import { useToast } from "../hooks/useToast";
import { formatDateTime } from "../utils/dateUtils";

export default function TicketDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { role } = useAuth();
  const { showToast } = useToast();
  const [ticket, setTicket] = useState(null);
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(true);

  const isAgentOrAdmin = useMemo(
    () => role === "AGENT" || role === "ADMIN",
    [role],
  );

  const loadTicket = useCallback(async () => {
    try {
      const { data } = await ticketApi.getById(id);
      setTicket(data.ticket);
      setMessages(data.messages || []);
    } catch (error) {
      showToast(
        error?.response?.data?.error || "Failed to load ticket.",
        "error",
      );
    } finally {
      setLoading(false);
    }
  }, [id, showToast]);

  useEffect(() => {
    loadTicket();
    const timer = setInterval(() => {
      ticketApi
        .messages(id)
        .then((res) => setMessages(res.data || []))
        .catch(() => {});
    }, 15000);
    return () => clearInterval(timer);
  }, [id, loadTicket]);

  const handleReply = async (e) => {
    e.preventDefault();
    const form = new FormData(e.currentTarget);
    const content = form.get("content");
    const isInternal = form.get("isInternal") === "on";

    try {
      await ticketApi.addMessage(id, { content, isInternal });
      e.currentTarget.reset();
      await loadTicket();
      showToast("Reply posted successfully.");
    } catch (error) {
      showToast(
        error?.response?.data?.error || "Failed to post reply.",
        "error",
      );
    }
  };

  const handleClose = async () => {
    try {
      await ticketApi.close(id);
      showToast("Ticket closed successfully.");
      await loadTicket();
    } catch (error) {
      showToast(
        error?.response?.data?.error || "Failed to close ticket.",
        "error",
      );
    }
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

  if (!ticket) {
    return (
      <>
        <Header />
        <div className="section">
          <h2>Ticket not found</h2>
          <button
            className="btn-primary"
            onClick={() => navigate("/dashboard")}
          >
            Go Back
          </button>
        </div>
        <Footer />
      </>
    );
  }

  return (
    <>
      <Header />
      <div className="details-page">
        <main className="details-container">
          <div style={{ marginBottom: "30px" }}>
            <h1 style={{ fontSize: "2rem", fontWeight: 600 }}>
              Ticket #{ticket.id}
            </h1>
            <p style={{ color: "var(--text-light)" }}>
              View complete information and communicate regarding this issue.
            </p>
          </div>

          <div
            style={{
              display: "grid",
              gridTemplateColumns: "minmax(0, 1fr) 300px",
              gap: "30px",
              alignItems: "start",
            }}
          >
            {/* Left Column: Conversation */}
            <div>
              {/* Description */}
              <section
                className="details-card"
                style={{ marginBottom: "30px" }}
              >
                <h3
                  style={{
                    borderBottom: "1px solid var(--border-light)",
                    paddingBottom: "12px",
                    marginBottom: "16px",
                  }}
                >
                  {ticket.title}
                </h3>
                <p style={{ color: "var(--text-dark)" }}>
                  {ticket.description}
                </p>
              </section>

              {/* Conversation Timeline */}
              <section
                className="details-card"
                style={{ marginBottom: "30px" }}
              >
                <h3 style={{ marginBottom: "24px" }}>Conversation Activity</h3>

                <div
                  style={{
                    display: "flex",
                    flexDirection: "column",
                    gap: "24px",
                  }}
                >
                  {messages.map((message) => (
                    <div
                      key={message.id}
                      style={{ display: "flex", gap: "16px" }}
                    >
                      <div
                        style={{
                          width: "40px",
                          height: "40px",
                          borderRadius: "50%",
                          background:
                            message.senderRole === "CUSTOMER"
                              ? "#eae4d9"
                              : "var(--primary)",
                          display: "flex",
                          alignItems: "center",
                          justifyContent: "center",
                          fontWeight: "bold",
                          color:
                            message.senderRole === "CUSTOMER"
                              ? "var(--primary)"
                              : "white",
                        }}
                      >
                        {message.senderRole?.slice(0, 1) || "U"}
                      </div>
                      <div style={{ flex: 1 }}>
                        <div
                          style={{
                            display: "flex",
                            justifyContent: "space-between",
                            marginBottom: "4px",
                          }}
                        >
                          <span style={{ fontWeight: 600 }}>
                            {message.senderRole}
                          </span>
                          <span
                            style={{
                              fontSize: "0.8rem",
                              color: "var(--text-light)",
                            }}
                          >
                            {formatDateTime(message.createdAt)}
                          </span>
                        </div>
                        <div
                          style={{
                            background: message.isInternal
                              ? "#f3f4f6"
                              : "#fdfaf6",
                            padding: "16px",
                            borderRadius: "0 12px 12px 12px",
                            border: "1px solid var(--border-light)",
                          }}
                        >
                          {message.isInternal && (
                            <p
                              style={{
                                fontSize: "0.8rem",
                                color: "#6b7280",
                                marginBottom: "6px",
                              }}
                            >
                              Internal note
                            </p>
                          )}
                          <p style={{ fontSize: "0.95rem" }}>
                            {message.content}
                          </p>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </section>

              {/* Reply Form */}
              <section className="details-card">
                <h3>Add a Reply</h3>
                <form onSubmit={handleReply} style={{ marginTop: "16px" }}>
                  <textarea
                    id="reply"
                    name="content"
                    rows="4"
                    placeholder="Write your message here..."
                    required
                  ></textarea>
                  {isAgentOrAdmin && (
                    <label style={{ marginBottom: "12px", display: "block" }}>
                      <input type="checkbox" name="isInternal" /> Mark as
                      internal note
                    </label>
                  )}
                  <div style={{ display: "flex", justifyContent: "flex-end" }}>
                    <button type="submit" className="btn-primary">
                      Post Reply
                    </button>
                  </div>
                </form>
              </section>
            </div>

            {/* Right Column: Metadata */}
            <aside>
              <section className="details-card">
                <h3 style={{ marginBottom: "20px" }}>Ticket Info</h3>
                <div
                  style={{
                    display: "flex",
                    flexDirection: "column",
                    gap: "16px",
                  }}
                >
                  <div>
                    <span
                      style={{
                        fontSize: "0.85rem",
                        color: "var(--text-light)",
                        display: "block",
                        marginBottom: "4px",
                      }}
                    >
                      Ticket ID
                    </span>
                    <span style={{ fontWeight: 500 }}>#{ticket.id}</span>
                  </div>

                  <div>
                    <span
                      style={{
                        fontSize: "0.85rem",
                        color: "var(--text-light)",
                        display: "block",
                        marginBottom: "4px",
                      }}
                    >
                      Category
                    </span>
                    <span style={{ fontWeight: 500 }}>{ticket.category}</span>
                  </div>

                  <div>
                    <span
                      style={{
                        fontSize: "0.85rem",
                        color: "var(--text-light)",
                        display: "block",
                        marginBottom: "4px",
                      }}
                    >
                      Status
                    </span>
                    <TicketStatusBadge status={ticket.status} />
                  </div>

                  <div>
                    <span
                      style={{
                        fontSize: "0.85rem",
                        color: "var(--text-light)",
                        display: "block",
                        marginBottom: "4px",
                      }}
                    >
                      Priority
                    </span>
                    <PriorityBadge priority={ticket.priority} />
                  </div>

                  <div>
                    <span
                      style={{
                        fontSize: "0.85rem",
                        color: "var(--text-light)",
                        display: "block",
                        marginBottom: "4px",
                      }}
                    >
                      Assigned Agent
                    </span>
                    <span style={{ fontWeight: 500 }}>
                      {ticket.assignedTo || "Unassigned"}
                    </span>
                  </div>

                  <div>
                    <span
                      style={{
                        fontSize: "0.85rem",
                        color: "var(--text-light)",
                        display: "block",
                        marginBottom: "4px",
                      }}
                    >
                      Date Submitted
                    </span>
                    <span style={{ fontWeight: 500 }}>
                      {formatDateTime(ticket.createdAt)}
                    </span>
                  </div>

                  {role === "CUSTOMER" && ticket.status === "RESOLVED" && (
                    <button
                      className="btn-outline"
                      type="button"
                      onClick={handleClose}
                    >
                      Close Ticket
                    </button>
                  )}

                  {isAgentOrAdmin && ticket.status !== "CLOSED" && (
                    <div style={{ marginTop: "16px" }}>
                      <span
                        style={{
                          fontSize: "0.85rem",
                          color: "var(--text-light)",
                          display: "block",
                          marginBottom: "8px",
                        }}
                      >
                        Update Status
                      </span>
                      <select
                        className="select-input"
                        value={ticket.status}
                        onChange={async (e) => {
                          try {
                            await ticketApi.updateStatus(ticket.id, { status: e.target.value });
                            showToast("Status updated successfully");
                            await loadTicket();
                          } catch (error) {
                            showToast(error?.response?.data?.error || "Failed to update status", "error");
                          }
                        }}
                        style={{ width: "100%", padding: "8px", borderRadius: "6px", border: "1px solid var(--border-light)" }}
                      >
                        <option value="OPEN">Open</option>
                        <option value="IN_PROGRESS">In Progress</option>
                        <option value="RESOLVED">Resolved</option>
                        <option value="CLOSED">Closed</option>
                      </select>
                    </div>
                  )}
                </div>
              </section>
            </aside>
          </div>
        </main>
      </div>
      <Footer />
    </>
  );
}
