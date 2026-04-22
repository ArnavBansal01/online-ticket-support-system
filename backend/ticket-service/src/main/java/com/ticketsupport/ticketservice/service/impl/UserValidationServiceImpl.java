package com.ticketsupport.ticketservice.service.impl;

import com.ticketsupport.ticketservice.exception.ForbiddenException;
import com.ticketsupport.ticketservice.exception.ResourceNotFoundException;
import com.ticketsupport.ticketservice.model.dto.response.UserLookupResponse;
import com.ticketsupport.ticketservice.service.UserValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserValidationServiceImpl implements UserValidationService {
    private final RestTemplate restTemplate;
    private final String userServiceBaseUrl;

    public UserValidationServiceImpl(RestTemplate restTemplate,
                                     @Value("${app.user-service.base-url}") String userServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.userServiceBaseUrl = userServiceBaseUrl;
    }

    @Override
    public UserLookupResponse getUserById(Long id, String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        ResponseEntity<UserLookupResponse> response = restTemplate.exchange(
                userServiceBaseUrl + "/api/users/" + id,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserLookupResponse.class
        );

        UserLookupResponse body = response.getBody();
        if (body == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return body;
    }

    @Override
    public UserLookupResponse validateAgent(Long id, String authHeader) {
        UserLookupResponse user = getUserById(id, authHeader);
        if (!"AGENT".equalsIgnoreCase(user.getRole())) {
            throw new ForbiddenException("Assigned user must be an AGENT");
        }
        return user;
    }
}
