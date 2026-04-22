package com.ticketsupport.ticketservice.model.dto.response;

import com.ticketsupport.ticketservice.model.entity.TicketAttachment;

import java.time.LocalDateTime;

public class TicketAttachmentResponse {
    private Long id;
    private Long ticketId;
    private Long messageId;
    private String fileName;
    private String fileUrl;
    private LocalDateTime uploadedAt;

    public static TicketAttachmentResponse fromEntity(TicketAttachment attachment) {
        TicketAttachmentResponse response = new TicketAttachmentResponse();
        response.id = attachment.getId();
        response.ticketId = attachment.getTicketId();
        response.messageId = attachment.getMessageId();
        response.fileName = attachment.getFileName();
        response.fileUrl = attachment.getFileUrl();
        response.uploadedAt = attachment.getUploadedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}
