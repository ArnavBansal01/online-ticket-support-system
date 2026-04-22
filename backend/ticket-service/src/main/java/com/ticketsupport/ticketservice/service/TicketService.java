package com.ticketsupport.ticketservice.service;

import com.ticketsupport.ticketservice.model.dto.request.AssignTicketRequest;
import com.ticketsupport.ticketservice.model.dto.request.CreateTicketRequest;
import com.ticketsupport.ticketservice.model.dto.request.UpdateTicketRequest;
import com.ticketsupport.ticketservice.model.dto.request.UpdateTicketStatusRequest;
import com.ticketsupport.ticketservice.model.dto.response.TicketDetailsResponse;
import com.ticketsupport.ticketservice.model.dto.response.TicketListResponse;
import com.ticketsupport.ticketservice.model.dto.response.TicketResponse;

import java.util.Map;

public interface TicketService {
    TicketResponse createTicket(CreateTicketRequest request, Long userId, String role);

    TicketListResponse listTickets(Map<String, String> filters, Long userId, String role);

    TicketDetailsResponse getTicket(Long id, Long userId, String role);

    TicketResponse updateTicket(Long id, UpdateTicketRequest request, Long userId, String role);

    TicketResponse updateStatus(Long id, UpdateTicketStatusRequest request, String role);

    TicketResponse assign(Long id, AssignTicketRequest request, String role, String authHeader);

    void softDelete(Long id, String role);

    TicketResponse closeTicket(Long id, Long userId, String role);
}
