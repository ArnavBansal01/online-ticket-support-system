package com.ticketsupport.ticketservice.controller;

import com.ticketsupport.ticketservice.model.dto.request.AddMessageRequest;
import com.ticketsupport.ticketservice.model.dto.response.TicketMessageResponse;
import com.ticketsupport.ticketservice.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{id}/messages")
public class TicketMessageController {
    private final MessageService messageService;

    public TicketMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public TicketMessageResponse add(@PathVariable("id") Long id,
                                     @Valid @RequestBody AddMessageRequest request,
                                     HttpServletRequest http) {
        return messageService.addMessage(id, request, (Long) http.getAttribute("userId"), (String) http.getAttribute("role"));
    }

    @GetMapping
    public List<TicketMessageResponse> list(@PathVariable("id") Long id, HttpServletRequest http) {
        return messageService.getMessages(id, (Long) http.getAttribute("userId"), (String) http.getAttribute("role"));
    }
}
