package com.ticketsupport.ticketservice.model.dto.response;

import java.time.LocalDateTime;

import com.ticketsupport.ticketservice.model.entity.Ticket;

public class TicketResponse {
    private Long id;
    private String title;
    private String description;
    private Ticket.Status status;
    private Ticket.Priority priority;
    private Ticket.Category category;
    private Long createdBy;
    private Long assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;

    public static TicketResponse fromEntity(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.id = ticket.getId();
        response.title = ticket.getTitle();
        response.description = ticket.getDescription();
        response.status = ticket.getStatus();
        response.priority = ticket.getPriority();
        response.category = ticket.getCategory();
        response.createdBy = ticket.getCreatedBy();
        response.assignedTo = ticket.getAssignedTo();
        response.createdAt = ticket.getCreatedAt();
        response.updatedAt = ticket.getUpdatedAt();
        response.resolvedAt = ticket.getResolvedAt();
        response.closedAt = ticket.getClosedAt();
        return response;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Ticket.Status getStatus() { return status; }
    public Ticket.Priority getPriority() { return priority; }
    public Ticket.Category getCategory() { return category; }
    public Long getCreatedBy() { return createdBy; }
    public Long getAssignedTo() { return assignedTo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public LocalDateTime getClosedAt() { return closedAt; }
}
