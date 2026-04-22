package com.ticketsupport.analyticsservice.service;

import java.util.List;
import java.util.Map;

public interface TicketClientService {
    List<Map<String, Object>> getAllTickets(String authHeader);
}
