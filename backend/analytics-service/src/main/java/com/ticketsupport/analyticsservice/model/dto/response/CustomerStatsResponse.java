package com.ticketsupport.analyticsservice.model.dto.response;

public class CustomerStatsResponse {
    private long openTickets;
    private long resolvedThisMonth;
    private double avgResponseTime;
    private long totalTickets;
    private String lastTicketDate;

    public CustomerStatsResponse(long openTickets, long resolvedThisMonth, double avgResponseTime, long totalTickets, String lastTicketDate) {
        this.openTickets = openTickets;
        this.resolvedThisMonth = resolvedThisMonth;
        this.avgResponseTime = avgResponseTime;
        this.totalTickets = totalTickets;
        this.lastTicketDate = lastTicketDate;
    }

    public long getOpenTickets() {
        return openTickets;
    }

    public long getResolvedThisMonth() {
        return resolvedThisMonth;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public long getTotalTickets() {
        return totalTickets;
    }

    public String getLastTicketDate() {
        return lastTicketDate;
    }
}
