package com.ticketsupport.ticketservice.model.dto.request;

import com.ticketsupport.ticketservice.model.entity.Ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateTicketRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Ticket.Priority priority;

    @NotNull
    private Ticket.Category category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Ticket.Priority getPriority() {
        return priority;
    }

    public void setPriority(Ticket.Priority priority) {
        this.priority = priority;
    }

    public Ticket.Category getCategory() {
        return category;
    }

    public void setCategory(Ticket.Category category) {
        this.category = category;
    }
}
