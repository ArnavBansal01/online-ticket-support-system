import { Link } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

function Header() {
  const { user, role, isAuthenticated, logout } = useAuth();

  const dashboardPath = role === "ADMIN" ? "/admin" : "/dashboard";

  return (
    <header>
      <div className="container nav-container">
        <div className="logo">
          <Link to="/" style={{ textDecoration: "none" }}>
            <h1>ResolveHub</h1>
          </Link>
          <p>Smart Ticket Support System</p>
        </div>
        <nav>
          {isAuthenticated && (
            <>
              <Link to={dashboardPath}>Dashboard</Link>
              <Link to="/dashboard">My Tickets</Link>
              <Link to="/raise-ticket">Raise Ticket</Link>
            </>
          )}
          <Link to="/contact">Contact</Link>
          {!isAuthenticated && <Link to="/login">Login</Link>}
          {!isAuthenticated && <Link to="/register">Register</Link>}
          {isAuthenticated && (
            <button type="button" className="btn-outline" onClick={logout}>
              Logout
            </button>
          )}
          {isAuthenticated && (
            <span
              style={{
                marginLeft: "12px",
                fontSize: "0.9rem",
                color: "var(--text-light)",
              }}
            >
              {user?.name} <span className="badge">{role}</span>
            </span>
          )}
        </nav>
      </div>
    </header>
  );
}

export default Header;
