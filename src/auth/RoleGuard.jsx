import { Navigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

export default function RoleGuard({ roles, children }) {
  const { role } = useAuth();
  if (!role || !roles.includes(role)) {
    return <Navigate to="/403" replace />;
  }
  return children;
}
