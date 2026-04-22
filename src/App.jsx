import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import RaiseTicket from "./pages/RaiseTicket";
import TicketDetails from "./pages/TicketDetails";
import UserDashboard from "./pages/UserDashboard";
import AdminDashboard from "./pages/AdminDashboard";
import Contact from "./pages/Contact";
import ForgotPassword from "./pages/ForgotPassword";
import Forbidden from "./pages/Forbidden";
import NotFound from "./pages/NotFound";
import ProtectedRoute from "./auth/ProtectedRoute";
import RoleGuard from "./auth/RoleGuard";
import Toast from "./components/Toast";
import { useToast } from "./hooks/useToast";

function App() {
  const { toast, clearToast } = useToast();

  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/403" element={<Forbidden />} />

          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <RoleGuard roles={["CUSTOMER", "AGENT", "ADMIN"]}>
                  <UserDashboard />
                </RoleGuard>
              </ProtectedRoute>
            }
          />
          <Route
            path="/user-dashboard"
            element={<Navigate to="/dashboard" replace />}
          />
          <Route
            path="/raise-ticket"
            element={
              <ProtectedRoute>
                <RaiseTicket />
              </ProtectedRoute>
            }
          />
          <Route
            path="/tickets/:id"
            element={
              <ProtectedRoute>
                <TicketDetails />
              </ProtectedRoute>
            }
          />
          <Route
            path="/ticket-details"
            element={<Navigate to="/dashboard" replace />}
          />
          <Route
            path="/admin"
            element={
              <ProtectedRoute>
                <RoleGuard roles={["ADMIN"]}>
                  <AdminDashboard />
                </RoleGuard>
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin-dashboard"
            element={<Navigate to="/admin" replace />}
          />

          <Route path="*" element={<NotFound />} />
        </Routes>
      </Router>
      <Toast toast={toast} onClose={clearToast} />
    </>
  );
}

export default App;
