import { useEffect, useReducer } from "react";
import { authService } from "./authService";
import { AuthContext } from "./authContextValue";

const initialState = {
  user: authService.getCurrentUser(),
  token: authService.getToken(),
};

function reducer(state, action) {
  switch (action.type) {
    case "LOGIN":
      return { user: action.payload.user, token: action.payload.token };
    case "LOGOUT":
      return { user: null, token: null };
    default:
      return state;
  }
}

export function AuthProvider({ children }) {
  const [state, dispatch] = useReducer(reducer, initialState);

  useEffect(() => {
    if (!authService.hasValidSession()) {
      dispatch({ type: "LOGOUT" });
    }
  }, []);

  const login = (payload) => dispatch({ type: "LOGIN", payload });

  const logout = () => {
    authService.logout();
    dispatch({ type: "LOGOUT" });
  };

  const value = {
    user: state.user,
    token: state.token,
    role: state.user?.role || null,
    isAuthenticated: Boolean(state.user && state.token),
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
