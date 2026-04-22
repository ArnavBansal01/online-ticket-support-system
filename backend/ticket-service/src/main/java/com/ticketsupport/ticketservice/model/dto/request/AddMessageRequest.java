package com.ticketsupport.ticketservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AddMessageRequest {
    @NotBlank
    private String content;

    private boolean isInternal;

    private String fileName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public void setInternal(boolean internal) {
        isInternal = internal;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
