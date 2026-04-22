package com.ticketsupport.analyticsservice.service.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ticketsupport.analyticsservice.service.TicketClientService;

@Service
public class TicketClientServiceImpl implements TicketClientService {
    private final RestTemplate restTemplate;
    private final String ticketServiceBaseUrl;

    public TicketClientServiceImpl(RestTemplate restTemplate,
                                   @Value("${app.ticket-service.base-url}") String ticketServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.ticketServiceBaseUrl = ticketServiceBaseUrl;
    }

    @Override
    public List<Map<String, Object>> getAllTickets(String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                ticketServiceBaseUrl + "/api/tickets?page=0&size=5000",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );

        Map<String, Object> body = response.getBody();
        if (body == null || body.get("items") == null) {
            return Collections.emptyList();
        }

        Object rawItems = body.get("items");
        if (!(rawItems instanceof List<?> itemsList)) {
            return Collections.emptyList();
        }

        return itemsList.stream()
                .filter(Map.class::isInstance)
                .map(item -> {
                    Map<?, ?> source = (Map<?, ?>) item;
                    Map<String, Object> normalized = new LinkedHashMap<>();
                    source.forEach((key, value) -> normalized.put(String.valueOf(key), value));
                    return normalized;
                })
                .toList();
    }
}
