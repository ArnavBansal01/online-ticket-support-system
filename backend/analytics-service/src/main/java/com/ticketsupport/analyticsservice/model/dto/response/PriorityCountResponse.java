package com.ticketsupport.analyticsservice.model.dto.response;

public class PriorityCountResponse {
    private String priority;
    private long count;

    public PriorityCountResponse(String priority, long count) {
        this.priority = priority;
        this.count = count;
    }

    public String getPriority() {
        return priority;
    }

    public long getCount() {
        return count;
    }
}
