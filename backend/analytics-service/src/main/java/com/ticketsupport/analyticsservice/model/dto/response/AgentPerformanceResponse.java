package com.ticketsupport.analyticsservice.model.dto.response;

public class AgentPerformanceResponse {
    private Long agentId;
    private String agentName;
    private long ticketsAssigned;
    private long ticketsResolved;
    private double avgResponseTime;

    public AgentPerformanceResponse(Long agentId, String agentName, long ticketsAssigned, long ticketsResolved, double avgResponseTime) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.ticketsAssigned = ticketsAssigned;
        this.ticketsResolved = ticketsResolved;
        this.avgResponseTime = avgResponseTime;
    }

    public Long getAgentId() {
        return agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public long getTicketsAssigned() {
        return ticketsAssigned;
    }

    public long getTicketsResolved() {
        return ticketsResolved;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }
}
