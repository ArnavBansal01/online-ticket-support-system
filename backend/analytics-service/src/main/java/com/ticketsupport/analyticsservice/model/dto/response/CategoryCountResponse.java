package com.ticketsupport.analyticsservice.model.dto.response;

public class CategoryCountResponse {
    private String category;
    private long count;

    public CategoryCountResponse(String category, long count) {
        this.category = category;
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public long getCount() {
        return count;
    }
}
