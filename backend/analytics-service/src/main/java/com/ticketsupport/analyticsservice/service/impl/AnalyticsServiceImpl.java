package com.ticketsupport.analyticsservice.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ticketsupport.analyticsservice.exception.ForbiddenException;
import com.ticketsupport.analyticsservice.model.dto.response.AgentPerformanceResponse;
import com.ticketsupport.analyticsservice.model.dto.response.CategoryCountResponse;
import com.ticketsupport.analyticsservice.model.dto.response.CustomerStatsResponse;
import com.ticketsupport.analyticsservice.model.dto.response.PriorityCountResponse;
import com.ticketsupport.analyticsservice.model.dto.response.StatusCountResponse;
import com.ticketsupport.analyticsservice.model.dto.response.SummaryResponse;
import com.ticketsupport.analyticsservice.model.dto.response.TrendPointResponse;
import com.ticketsupport.analyticsservice.service.AnalyticsService;
import com.ticketsupport.analyticsservice.service.TicketClientService;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    private final TicketClientService ticketClientService;
    private final RestTemplate restTemplate;
    private final String userServiceBaseUrl;

    public AnalyticsServiceImpl(TicketClientService ticketClientService,
                                RestTemplate restTemplate,
                                @Value("${app.user-service.base-url}") String userServiceBaseUrl) {
        this.ticketClientService = ticketClientService;
        this.restTemplate = restTemplate;
        this.userServiceBaseUrl = userServiceBaseUrl;
    }

    @Override
    public SummaryResponse summary(String authHeader) {
        List<Map<String, Object>> tickets = ticketClientService.getAllTickets(authHeader);
        long total = tickets.size();
        long open = tickets.stream().filter(t -> "OPEN".equals(t.get("status")) || "IN_PROGRESS".equals(t.get("status"))).count();
        long resolved = tickets.stream().filter(t -> "RESOLVED".equals(t.get("status")) || "CLOSED".equals(t.get("status"))).count();

        List<Double> resolutionTimes = tickets.stream()
                .filter(t -> t.get("resolvedAt") != null)
                .map(this::resolutionHours)
                .filter(Objects::nonNull)
                .toList();
        double avgResolution = resolutionTimes.isEmpty() ? 0 : resolutionTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        long slaBreachCount = tickets.stream()
                .map(this::resolutionHours)
                .filter(Objects::nonNull)
                .filter(hours -> hours > 48)
                .count();

        return new SummaryResponse(total, open, resolved, avgResolution, slaBreachCount);
    }

    @Override
    public List<StatusCountResponse> byStatus(String authHeader) {
        return group(ticketClientService.getAllTickets(authHeader), "status").entrySet().stream()
                .map(e -> new StatusCountResponse(e.getKey(), e.getValue()))
                .toList();
    }

    @Override
    public List<PriorityCountResponse> byPriority(String authHeader) {
        return group(ticketClientService.getAllTickets(authHeader), "priority").entrySet().stream()
                .map(e -> new PriorityCountResponse(e.getKey(), e.getValue()))
                .toList();
    }

    @Override
    public List<CategoryCountResponse> byCategory(String authHeader) {
        return group(ticketClientService.getAllTickets(authHeader), "category").entrySet().stream()
                .map(e -> new CategoryCountResponse(e.getKey(), e.getValue()))
                .toList();
    }

    @Override
    public List<TrendPointResponse> trend(String period, String authHeader) {
        int days = switch (period == null ? "30d" : period) {
            case "7d" -> 7;
            case "90d" -> 90;
            default -> 30;
        };

        LocalDate start = LocalDate.now().minusDays(days - 1L);
        Map<LocalDate, Long> counts = new LinkedHashMap<>();
        for (int i = 0; i < days; i++) {
            counts.put(start.plusDays(i), 0L);
        }

        for (Map<String, Object> ticket : ticketClientService.getAllTickets(authHeader)) {
            String createdAt = (String) ticket.get("createdAt");
            if (createdAt == null) {
                continue;
            }
            LocalDate date = LocalDateTime.parse(createdAt).toLocalDate();
            if (!date.isBefore(start) && counts.containsKey(date)) {
                counts.put(date, counts.get(date) + 1);
            }
        }

        DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE;
        return counts.entrySet().stream()
                .map(e -> new TrendPointResponse(e.getKey().format(fmt), e.getValue()))
                .toList();
    }

    @Override
    public List<AgentPerformanceResponse> agentPerformance(String role, String authHeader) {
        if (!"ADMIN".equals(role)) {
            throw new ForbiddenException("Admin role required");
        }

        List<Map<String, Object>> tickets = ticketClientService.getAllTickets(authHeader);
        Map<Long, List<Map<String, Object>>> grouped = new LinkedHashMap<>();
        for (Map<String, Object> ticket : tickets) {
            Number assignee = (Number) ticket.get("assignedTo");
            if (assignee != null) {
                grouped.computeIfAbsent(assignee.longValue(), k -> new ArrayList<>()).add(ticket);
            }
        }

        return grouped.entrySet().stream().map(entry -> {
            long assigned = entry.getValue().size();
            long resolved = entry.getValue().stream().filter(t -> "RESOLVED".equals(t.get("status")) || "CLOSED".equals(t.get("status"))).count();
            double avg = entry.getValue().stream().map(this::resolutionHours).filter(Objects::nonNull).mapToDouble(Double::doubleValue).average().orElse(0);
            return new AgentPerformanceResponse(entry.getKey(), resolveAgentName(entry.getKey(), authHeader), assigned, resolved, avg);
        }).sorted(Comparator.comparing(AgentPerformanceResponse::getAgentId)).toList();
    }

    @Override
    public CustomerStatsResponse customerStats(Long userId, String authHeader) {
        List<Map<String, Object>> mine = ticketClientService.getAllTickets(authHeader).stream()
                .filter(t -> {
                    Number createdBy = (Number) t.get("createdBy");
                    Number assignedTo = (Number) t.get("assignedTo");
                    boolean isCreator = createdBy != null && createdBy.longValue() == userId;
                    boolean isAssignee = assignedTo != null && assignedTo.longValue() == userId;
                    return isCreator || isAssignee;
                })
                .toList();

        long open = mine.stream().filter(t -> "OPEN".equals(t.get("status")) || "IN_PROGRESS".equals(t.get("status"))).count();
        long resolvedThisMonth = mine.stream()
                .filter(t -> {
                    String resolvedAt = (String) t.get("resolvedAt");
                    return resolvedAt != null && LocalDateTime.parse(resolvedAt).getMonth().equals(LocalDate.now().getMonth());
                }).count();

        double avgResponse = mine.stream().map(this::resolutionHours).filter(Objects::nonNull).mapToDouble(Double::doubleValue).average().orElse(0);

        String lastTicketDate = mine.stream()
                .map(t -> (String) t.get("createdAt"))
                .filter(Objects::nonNull)
                .max(String::compareTo)
                .orElse(null);

        return new CustomerStatsResponse(open, resolvedThisMonth, avgResponse, mine.size(), lastTicketDate);
    }

    private Map<String, Long> group(List<Map<String, Object>> tickets, String key) {
        Map<String, Long> out = new LinkedHashMap<>();
        for (Map<String, Object> t : tickets) {
            String value = String.valueOf(t.get(key));
            out.put(value, out.getOrDefault(value, 0L) + 1);
        }
        return out;
    }

    private Double resolutionHours(Map<String, Object> t) {
        try {
            String createdAt = (String) t.get("createdAt");
            String resolvedAt = (String) t.get("resolvedAt");
            if (createdAt == null || resolvedAt == null) {
                return null;
            }
            return (double) Duration.between(LocalDateTime.parse(createdAt), LocalDateTime.parse(resolvedAt)).toHours();
        } catch (Exception ignored) {
            return null;
        }
    }

    private String resolveAgentName(Long agentId, String authHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            ResponseEntity<Map> response = restTemplate.exchange(
                    userServiceBaseUrl + "/api/users/" + agentId,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Map.class
            );
            Object name = response.getBody() == null ? null : response.getBody().get("name");
            return name == null ? "Agent " + agentId : String.valueOf(name);
        } catch (Exception ignored) {
            return "Agent " + agentId;
        }
    }
}
