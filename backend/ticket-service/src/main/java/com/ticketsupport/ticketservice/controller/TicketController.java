package com.ticketsupport.ticketservice.controller;

import com.ticketsupport.ticketservice.model.dto.request.AssignTicketRequest;
import com.ticketsupport.ticketservice.model.dto.request.CreateTicketRequest;
import com.ticketsupport.ticketservice.model.dto.request.UpdateTicketRequest;
import com.ticketsupport.ticketservice.model.dto.request.UpdateTicketStatusRequest;
import com.ticketsupport.ticketservice.model.dto.response.TicketDetailsResponse;
import com.ticketsupport.ticketservice.model.dto.response.TicketListResponse;
import com.ticketsupport.ticketservice.model.dto.response.TicketResponse;
import com.ticketsupport.ticketservice.service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public TicketResponse create(@Valid @RequestBody CreateTicketRequest request, HttpServletRequest http) {
        return ticketService.createTicket(request, (Long) http.getAttribute("userId"), (String) http.getAttribute("role"));
    }

    @GetMapping
    public TicketListResponse list(@RequestParam Map<String, String> params, HttpServletRequest http) {
        return ticketService.listTickets(params, (Long) http.getAttribute("userId"), (String) http.getAttribute("role"));
    }

    @GetMapping("/{id}")
    public TicketDetailsResponse get(@PathVariable Long id, HttpServletRequest http) {
        return ticketService.getTicket(id, (Long) http.getAttribute("userId"), (String) http.getAttribute("role"));
    }

    @PutMapping("/{id}")
    public TicketResponse update(@PathVariable Long id, @Valid @RequestBody UpdateTicketRequest request, HttpServletRequest http) {
        return ticketService.updateTicket(id, request, (Long) http.getAttribute("userId"), (String) http.getAttribute("role"));
    }

    @PutMapping("/{id}/status")
    public TicketResponse status(@PathVariable Long id, @Valid @RequestBody UpdateTicketStatusRequest request, HttpServletRequest http) {
        return ticketService.updateStatus(id, request, (String) http.getAttribute("role"));
    }

    @PutMapping("/{id}/assign")
    public TicketResponse assign(@PathVariable Long id,
                                 @Valid @RequestBody AssignTicketRequest request,
                                 HttpServletRequest http,
                                 @RequestHeader("Authorization") String authorization) {
        return ticketService.assign(id, request, (String) http.getAttribute("role"), authorization);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id, HttpServletRequest http) {
        ticketService.softDelete(id, (String) http.getAttribute("role"));
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Ticket deleted");
        response.put("deleted", true);
        return response;
    }

    @PutMapping("/{id}/close")
    public TicketResponse close(@PathVariable Long id, HttpServletRequest http) {
        return ticketService.closeTicket(id, (Long) http.getAttribute("userId"), (String) http.getAttribute("role"));
    }
}
