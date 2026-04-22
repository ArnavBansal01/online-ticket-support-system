import { authApi } from "../api/authApi";
import { isTokenExpired } from "../utils/jwt";

const TOKEN_KEY = "auth_token";
const USER_KEY = "auth_user";

export const authService = {
  async login(payload) {
    const { data } = await authApi.login(payload);
    localStorage.setItem(TOKEN_KEY, data.token);
    localStorage.setItem(USER_KEY, JSON.stringify(data.user));
    return data;
  },

  async register(payload) {
    const { data } = await authApi.register(payload);
    localStorage.setItem(TOKEN_KEY, data.token);
    localStorage.setItem(USER_KEY, JSON.stringify(data.user));
    return data;
  },

  logout() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  },

  getToken() {
    return localStorage.getItem(TOKEN_KEY);
  },

  getCurrentUser() {
    const raw = localStorage.getItem(USER_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw);
    } catch {
      return null;
    }
  },

  hasValidSession() {
    const token = this.getToken();
    if (!token) return false;
    if (isTokenExpired(token)) {
      this.logout();
      return false;
    }
    return true;
  },
};
