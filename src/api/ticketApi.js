import { ticketClient } from "./api";

export const ticketApi = {
  list: (params) => ticketClient.get("/api/tickets", { params }),
  getById: (id) => ticketClient.get(`/api/tickets/${id}`),
  create: (payload) => ticketClient.post("/api/tickets", payload),
  update: (id, payload) => ticketClient.put(`/api/tickets/${id}`, payload),
  updateStatus: (id, payload) =>
    ticketClient.put(`/api/tickets/${id}/status`, payload),
  assign: (id, payload) =>
    ticketClient.put(`/api/tickets/${id}/assign`, payload),
  remove: (id) => ticketClient.delete(`/api/tickets/${id}`),
  close: (id) => ticketClient.put(`/api/tickets/${id}/close`),
  addMessage: (id, payload) =>
    ticketClient.post(`/api/tickets/${id}/messages`, payload),
  messages: (id) => ticketClient.get(`/api/tickets/${id}/messages`),
};
