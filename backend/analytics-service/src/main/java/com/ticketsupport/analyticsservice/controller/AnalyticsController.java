package com.ticketsupport.analyticsservice.controller;

import com.ticketsupport.analyticsservice.model.dto.response.AgentPerformanceResponse;
import com.ticketsupport.analyticsservice.model.dto.response.CategoryCountResponse;
import com.ticketsupport.analyticsservice.model.dto.response.CustomerStatsResponse;
import com.ticketsupport.analyticsservice.model.dto.response.PriorityCountResponse;
import com.ticketsupport.analyticsservice.model.dto.response.StatusCountResponse;
import com.ticketsupport.analyticsservice.model.dto.response.SummaryResponse;
import com.ticketsupport.analyticsservice.model.dto.response.TrendPointResponse;
import com.ticketsupport.analyticsservice.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public SummaryResponse summary(@RequestHeader("Authorization") String authHeader) {
        return analyticsService.summary(authHeader);
    }

    @GetMapping("/by-status")
    public List<StatusCountResponse> byStatus(@RequestHeader("Authorization") String authHeader) {
        return analyticsService.byStatus(authHeader);
    }

    @GetMapping("/by-priority")
    public List<PriorityCountResponse> byPriority(@RequestHeader("Authorization") String authHeader) {
        return analyticsService.byPriority(authHeader);
    }

    @GetMapping("/by-category")
    public List<CategoryCountResponse> byCategory(@RequestHeader("Authorization") String authHeader) {
        return analyticsService.byCategory(authHeader);
    }

    @GetMapping("/trend")
    public List<TrendPointResponse> trend(@RequestParam(defaultValue = "30d") String period,
                                          @RequestHeader("Authorization") String authHeader) {
        return analyticsService.trend(period, authHeader);
    }

    @GetMapping("/agent-performance")
    public List<AgentPerformanceResponse> agentPerformance(HttpServletRequest request,
                                                           @RequestHeader("Authorization") String authHeader) {
        return analyticsService.agentPerformance((String) request.getAttribute("role"), authHeader);
    }

    @GetMapping("/customer-stats/{userId}")
    public CustomerStatsResponse customerStats(@PathVariable Long userId,
                                               @RequestHeader("Authorization") String authHeader) {
        return analyticsService.customerStats(userId, authHeader);
    }
}
