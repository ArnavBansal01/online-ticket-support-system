package com.ticketsupport.analyticsservice.model.dto.response;

public class TrendPointResponse {
    private String date;
    private long count;

    public TrendPointResponse(String date, long count) {
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public long getCount() {
        return count;
    }
}
