import Header from "../components/Header";
import Footer from "../components/Footer";
import { ticketApi } from "../api/ticketApi";
import { useNavigate } from "react-router-dom";
import { useToast } from "../hooks/useToast";

function RaiseTicket() {
  const navigate = useNavigate();
  const { showToast } = useToast();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const form = new FormData(e.currentTarget);

    const payload = {
      title: form.get("title"),
      category: form.get("category"),
      priority: form.get("priority"),
      description: form.get("description"),
      fileName: form.get("fileName") || null,
    };

    if (!payload.description || payload.description.length < 20) {
      showToast("Description must be at least 20 characters.", "error");
      return;
    }

    try {
      const { data } = await ticketApi.create(payload);
      showToast("Ticket submitted successfully.");
      navigate(`/tickets/${data.id}`);
    } catch (error) {
      showToast(
        error?.response?.data?.error || "Failed to submit ticket.",
        "error",
      );
    }
  };

  return (
    <>
      <Header />
      <div className="user-page">
        <main className="user-container" style={{ maxWidth: "800px" }}>
          <div
            className="user-header"
            style={{ textAlign: "center", marginBottom: "40px" }}
          >
            <h1>Submit a New Support Ticket</h1>
            <p style={{ color: "var(--text-light)", marginTop: "10px" }}>
              Describe your issue clearly and our support team will assist you
              as soon as possible.
            </p>
          </div>

          <section className="ticket-card">
            <form onSubmit={handleSubmit}>
              <label htmlFor="title">Issue Title</label>
              <input
                id="title"
                name="title"
                type="text"
                placeholder="Enter a short title for your issue"
                required
              />

              <label htmlFor="category">Select Category</label>
              <select id="category" name="category">
                <option value="TECHNICAL">Technical Issue</option>
                <option value="BILLING">Billing Issue</option>
                <option value="GENERAL">General Inquiry</option>
                <option value="FEATURE_REQUEST">Feature Request</option>
              </select>

              <label htmlFor="priority">Priority</label>
              <select id="priority" name="priority">
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
                <option value="URGENT">Urgent</option>
              </select>

              <label htmlFor="description">Issue Description</label>
              <textarea
                id="description"
                name="description"
                rows="4"
                placeholder="Describe your issue here..."
                required
              ></textarea>

              <label htmlFor="fileName">
                Attach Supporting File (filename only)
              </label>
              <input
                id="fileName"
                name="fileName"
                type="text"
                placeholder="error-log.txt"
              />

              <button type="submit" className="btn-primary">
                Submit Ticket
              </button>
            </form>
          </section>
        </main>
      </div>
      <Footer />
    </>
  );
}

export default RaiseTicket;
