package com.ticketsupport.ticketservice.model.dto.response;

import java.util.List;

public class TicketDetailsResponse {
    private TicketResponse ticket;
    private List<TicketMessageResponse> messages;
    private List<TicketAttachmentResponse> attachments;

    public TicketDetailsResponse(TicketResponse ticket, List<TicketMessageResponse> messages, List<TicketAttachmentResponse> attachments) {
        this.ticket = ticket;
        this.messages = messages;
        this.attachments = attachments;
    }

    public TicketResponse getTicket() {
        return ticket;
    }

    public List<TicketMessageResponse> getMessages() {
        return messages;
    }

    public List<TicketAttachmentResponse> getAttachments() {
        return attachments;
    }
}
