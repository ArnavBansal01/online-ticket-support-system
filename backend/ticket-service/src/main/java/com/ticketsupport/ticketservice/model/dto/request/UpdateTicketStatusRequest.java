package com.ticketsupport.ticketservice.model.dto.request;

import com.ticketsupport.ticketservice.model.entity.Ticket;
import jakarta.validation.constraints.NotNull;

public class UpdateTicketStatusRequest {
    @NotNull
    private Ticket.Status status;

    public Ticket.Status getStatus() {
        return status;
    }

    public void setStatus(Ticket.Status status) {
        this.status = status;
    }
}
