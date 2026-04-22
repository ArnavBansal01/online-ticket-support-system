package com.ticketsupport.ticketservice.service;

import com.ticketsupport.ticketservice.model.dto.response.UserLookupResponse;

public interface UserValidationService {
    UserLookupResponse getUserById(Long id, String authHeader);

    UserLookupResponse validateAgent(Long id, String authHeader);
}
