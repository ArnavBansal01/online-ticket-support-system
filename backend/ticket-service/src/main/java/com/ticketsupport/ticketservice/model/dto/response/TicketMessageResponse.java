package com.ticketsupport.ticketservice.model.dto.response;

import com.ticketsupport.ticketservice.model.entity.TicketMessage;

import java.time.LocalDateTime;
import java.util.List;

public class TicketMessageResponse {
    private Long id;
    private Long ticketId;
    private Long senderId;
    private String senderRole;
    private String content;
    private LocalDateTime createdAt;
    private boolean isInternal;
    private List<TicketAttachmentResponse> attachments;

    public static TicketMessageResponse fromEntity(TicketMessage message, List<TicketAttachmentResponse> attachments) {
        TicketMessageResponse response = new TicketMessageResponse();
        response.id = message.getId();
        response.ticketId = message.getTicketId();
        response.senderId = message.getSenderId();
        response.senderRole = message.getSenderRole();
        response.content = message.getContent();
        response.createdAt = message.getCreatedAt();
        response.isInternal = message.isInternal();
        response.attachments = attachments;
        return response;
    }

    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderRole() {
        return senderRole;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public List<TicketAttachmentResponse> getAttachments() {
        return attachments;
    }
}
