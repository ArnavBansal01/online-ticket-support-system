package com.ticketsupport.analyticsservice.model.dto.response;

public class SummaryResponse {
    private long totalTickets;
    private long openCount;
    private long resolvedCount;
    private double avgResolutionTime;
    private long slaBreachCount;

    public SummaryResponse(long totalTickets, long openCount, long resolvedCount, double avgResolutionTime, long slaBreachCount) {
        this.totalTickets = totalTickets;
        this.openCount = openCount;
        this.resolvedCount = resolvedCount;
        this.avgResolutionTime = avgResolutionTime;
        this.slaBreachCount = slaBreachCount;
    }

    public long getTotalTickets() { return totalTickets; }
    public long getOpenCount() { return openCount; }
    public long getResolvedCount() { return resolvedCount; }
    public double getAvgResolutionTime() { return avgResolutionTime; }
    public long getSlaBreachCount() { return slaBreachCount; }
}
