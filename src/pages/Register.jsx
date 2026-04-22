import { Link } from "react-router-dom";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header";
import Footer from "../components/Footer";
import { authService } from "../auth/authService";
import { useAuth } from "../hooks/useAuth";
import { useToast } from "../hooks/useToast";

function Register() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const { showToast } = useToast();
  const [error, setError] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();
    const form = new FormData(e.currentTarget);

    const name = form.get("name");
    const email = form.get("email");
    const password = form.get("password");
    const confirmPassword = form.get("confirmPassword");

    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    try {
      const data = await authService.register({ name, email, password });
      login({ user: data.user, token: data.token });
      showToast("Registration successful.");
      setError("");
      navigate("/dashboard");
    } catch (err) {
      const message = err?.response?.data?.error || "Registration failed";
      setError(message);
      showToast(message, "error");
    }
  };

  return (
    <>
      <Header />
      <div className="register-page">
        <main className="register-container">
          <h1>Create Your Account</h1>
          <p>
            Register to submit and track support tickets efficiently through
            your personal dashboard.
          </p>

          <section className="register-card">
            <form onSubmit={handleRegister}>
              {error && (
                <p style={{ color: "#dc2626", marginBottom: "12px" }}>
                  {error}
                </p>
              )}
              <label htmlFor="name">Full Name</label>
              <input
                id="name"
                name="name"
                type="text"
                placeholder="Enter your full name"
                required
              />

              <label htmlFor="email">Email Address</label>
              <input
                id="email"
                name="email"
                type="email"
                placeholder="Enter your email address"
                required
              />

              <label htmlFor="pass">Password</label>
              <input
                id="pass"
                name="password"
                type="password"
                placeholder="Create a password"
                required
              />

              <label htmlFor="confirm">Confirm Password</label>
              <input
                id="confirm"
                name="confirmPassword"
                type="password"
                placeholder="Re-enter your password"
                required
              />

              <label className="terms">
                <input type="checkbox" required />I agree to the terms and
                conditions
              </label>

              <button type="submit" className="btn-primary">
                Create Account
              </button>

              <p className="login-link">
                Already have an account? <Link to="/login">Login here</Link>
              </p>
            </form>
          </section>
        </main>
      </div>
      <Footer />
    </>
  );
}

export default Register;
