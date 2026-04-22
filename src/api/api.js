import axios from "axios";

const serviceBaseUrl = (url) => `${url}`;

export const userClient = axios.create({
  baseURL: serviceBaseUrl("http://localhost:8081"),
});

export const ticketClient = axios.create({
  baseURL: serviceBaseUrl("http://localhost:8082"),
});

export const analyticsClient = axios.create({
  baseURL: serviceBaseUrl("http://localhost:8083"),
});

const clients = [userClient, ticketClient, analyticsClient];

clients.forEach((client) => {
  client.interceptors.request.use((config) => {
    const token = localStorage.getItem("auth_token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });

  client.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error?.response?.status === 401) {
        localStorage.removeItem("auth_token");
        localStorage.removeItem("auth_user");
        if (window.location.pathname !== "/login") {
          window.location.href = "/login";
        }
      }
      return Promise.reject(error);
    },
  );
});
