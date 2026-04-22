package com.ticketsupport.ticketservice.model.dto.request;

import jakarta.validation.constraints.NotNull;

public class AssignTicketRequest {
    @NotNull
    private Long agentId;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
}
