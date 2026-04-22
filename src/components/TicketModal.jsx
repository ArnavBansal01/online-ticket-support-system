import React from "react";
import { ticketApi } from "../api/ticketApi";
import { useToast } from "../hooks/useToast";

export default function TicketModal({ isOpen, onClose, onCreated }) {
  const { showToast } = useToast();

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    const payload = {
      title: formData.get("title"),
      category: formData.get("category"),
      priority: formData.get("priority"),
      description: formData.get("description"),
      fileName: formData.get("fileName") || null,
    };

    if (!payload.description || payload.description.length < 20) {
      showToast("Description must be at least 20 characters.", "error");
      return;
    }

    try {
      const { data } = await ticketApi.create(payload);
      showToast("Ticket created successfully.");
      onCreated?.(data);
      onClose();
    } catch (error) {
      showToast(
        error?.response?.data?.error || "Failed to create ticket.",
        "error",
      );
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <button className="close-btn" onClick={onClose}>
          &times;
        </button>
        <h2 style={{ marginBottom: "10px" }}>Create New Ticket</h2>
        <p
          style={{
            color: "var(--text-light)",
            marginBottom: "24px",
            fontSize: "0.95rem",
          }}
        >
          Describe your issue clearly below.
        </p>

        <form onSubmit={handleSubmit}>
          <label htmlFor="title">Issue Title</label>
          <input
            id="title"
            name="title"
            type="text"
            placeholder="Short description..."
            required
            autoFocus
          />

          <label htmlFor="category">Category</label>
          <select id="category" name="category">
            <option value="TECHNICAL">Technical Issue</option>
            <option value="BILLING">Billing Issue</option>
            <option value="GENERAL">General Inquiry</option>
            <option value="FEATURE_REQUEST">Feature Request</option>
          </select>

          <label>Priority</label>
          <select id="priority" name="priority">
            <option value="LOW">Low</option>
            <option value="MEDIUM">Medium</option>
            <option value="HIGH">High</option>
            <option value="URGENT">Urgent</option>
          </select>

          <label htmlFor="description">Details</label>
          <textarea
            id="description"
            name="description"
            rows="4"
            placeholder="Elaborate on the issue..."
            required
          ></textarea>

          <label htmlFor="fileName">File Name (optional)</label>
          <input
            id="fileName"
            name="fileName"
            type="text"
            placeholder="screenshot.png"
          />

          <button
            type="submit"
            className="btn-primary"
            style={{ width: "100%", marginTop: "10px" }}
          >
            Submit Ticket
          </button>
        </form>
      </div>
    </div>
  );
}
