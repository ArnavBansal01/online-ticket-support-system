package com.ticketsupport.analyticsservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Cors cors = new Cors();
    private final TicketService ticketService = new TicketService();
    private final UserService userService = new UserService();

    public Cors getCors() {
        return cors;
    }

    public TicketService getTicketService() {
        return ticketService;
    }

    public UserService getUserService() {
        return userService;
    }

    public static class Cors {
        private String allowedOrigin;

        public String getAllowedOrigin() {
            return allowedOrigin;
        }

        public void setAllowedOrigin(String allowedOrigin) {
            this.allowedOrigin = allowedOrigin;
        }
    }

    public static class TicketService {
        private String baseUrl;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }

    public static class UserService {
        private String baseUrl;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }
}
