import { userClient } from "./api";

export const userApi = {
  listUsers: () => userClient.get("/api/users"),
  getUser: (id) => userClient.get(`/api/users/${id}`),
  updateUser: (id, payload) => userClient.put(`/api/users/${id}`, payload),
  updateRole: (id, payload) => userClient.put(`/api/users/${id}/role`, payload),
  deactivate: (id) => userClient.delete(`/api/users/${id}`),
};
