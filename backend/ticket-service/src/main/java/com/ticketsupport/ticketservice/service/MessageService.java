package com.ticketsupport.ticketservice.service;

import com.ticketsupport.ticketservice.model.dto.request.AddMessageRequest;
import com.ticketsupport.ticketservice.model.dto.response.TicketMessageResponse;

import java.util.List;

public interface MessageService {
    TicketMessageResponse addMessage(Long ticketId, AddMessageRequest request, Long senderId, String role);

    List<TicketMessageResponse> getMessages(Long ticketId, Long userId, String role);
}
