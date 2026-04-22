import { userClient } from "./api";

export const authApi = {
  register: (payload) => userClient.post("/api/auth/register", payload),
  login: (payload) => userClient.post("/api/auth/login", payload),
  me: () => userClient.get("/api/auth/me"),
};
