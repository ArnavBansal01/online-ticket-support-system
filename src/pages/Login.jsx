import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import Header from "../components/Header";
import Footer from "../components/Footer";
import { authService } from "../auth/authService";
import { useAuth } from "../hooks/useAuth";
import { useToast } from "../hooks/useToast";

function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const { showToast } = useToast();
  const [error, setError] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    const form = new FormData(e.currentTarget);

    try {
      const data = await authService.login({
        email: form.get("email"),
        password: form.get("password"),
      });

      login({ user: data.user, token: data.token });
      setError("");
      navigate(data.user.role === "ADMIN" ? "/admin" : "/dashboard");
    } catch (err) {
      const message = err?.response?.data?.error || "Login failed";
      setError(message);
      showToast(message, "error");
    }
  };

  return (
    <>
      <Header />
      <div className="login-page">
        <main className="login-container">
          <h1>Login to Your Account</h1>
          <p>Access your dashboard to track and manage your support tickets.</p>

          <section className="login-card">
            <form onSubmit={handleLogin}>
              {error && (
                <p style={{ color: "#dc2626", marginBottom: "12px" }}>
                  {error}
                </p>
              )}
              <label htmlFor="email">Email Address</label>
              <input
                id="email"
                name="email"
                type="email"
                placeholder="Enter your registered email"
                required
              />

              <label htmlFor="password">Password</label>
              <input
                id="password"
                name="password"
                type="password"
                placeholder="Enter your password"
                required
              />

              <div className="form-options">
                <label className="remember">
                  <input type="checkbox" /> Remember me
                </label>
                <Link to="/forgot-password">Forgot Password?</Link>
              </div>
              <button
                type="submit"
                className="btn-primary"
                style={{
                  textAlign: "center",
                  width: "100%",
                  marginBottom: "15px",
                }}
              >
                Login
              </button>

              <p className="register-link">
                Don't have an account? <Link to="/register">Register here</Link>
              </p>
            </form>
          </section>
        </main>
      </div>
      <Footer />
    </>
  );
}

export default Login;
