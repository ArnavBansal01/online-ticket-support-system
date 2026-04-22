import Header from "../components/Header";
import Footer from "../components/Footer";
import { ticketApi } from "../api/ticketApi";
import { useToast } from "../hooks/useToast";

function Contact() {
  const { showToast } = useToast();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const form = new FormData(e.currentTarget);

    try {
      await ticketApi.create({
        title: "Contact Form Inquiry",
        category: "GENERAL",
        priority: "LOW",
        description: `Name: ${form.get("name")}\nEmail: ${form.get("email")}\nSubject: ${form.get("subject")}\nMessage: ${form.get("message")}`,
      });
      showToast("Your message has been submitted.");
      e.currentTarget.reset();
    } catch (error) {
      showToast(
        error?.response?.data?.error || "Failed to submit contact form.",
        "error",
      );
    }
  };

  return (
    <>
      <Header />
      <div className="contact-page">
        <div
          className="user-header"
          style={{ textAlign: "center", marginBottom: "40px" }}
        >
          <h1>Contact Support</h1>
          <p style={{ color: "var(--text-light)", marginTop: "10px" }}>
            If you have any questions or need assistance, feel free to reach out
            to our support team.
          </p>
        </div>
        <main
          className="user-container"
          style={{
            display: "flex",
            gap: "30px",
            flexWrap: "wrap",
            justifyContent: "center",
          }}
        >
          <section className="contact-info">
            <h2>Contact Information</h2>
            <ul>
              <li>Email: support@resolvehub.com</li>
              <li>Phone: +91-XXXXXXXXXX</li>
              <li>Office Address: Chandigarh, India</li>
            </ul>
          </section>
          <section className="contact-form">
            <form onSubmit={handleSubmit}>
              <label htmlFor="name">Full Name</label>
              <input id="name" name="name" type="text" required />

              <label htmlFor="email">Email</label>
              <input id="email" name="email" type="email" required />

              <label htmlFor="subject">Subject</label>
              <input id="subject" name="subject" type="text" required />

              <label htmlFor="message">Message</label>
              <textarea
                id="message"
                name="message"
                rows="4"
                required
              ></textarea>

              <button type="submit" className="btn-primary">
                Submit
              </button>
            </form>
          </section>
        </main>
      </div>
      <Footer />
    </>
  );
}

export default Contact;
