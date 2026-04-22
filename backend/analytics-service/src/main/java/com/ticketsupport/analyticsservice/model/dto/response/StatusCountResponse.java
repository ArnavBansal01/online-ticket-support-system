package com.ticketsupport.analyticsservice.model.dto.response;

public class StatusCountResponse {
    private String status;
    private long count;

    public StatusCountResponse(String status, long count) {
        this.status = status;
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public long getCount() {
        return count;
    }
}
