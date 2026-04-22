package com.ticketsupport.analyticsservice.service;

import com.ticketsupport.analyticsservice.model.dto.response.AgentPerformanceResponse;
import com.ticketsupport.analyticsservice.model.dto.response.CategoryCountResponse;
import com.ticketsupport.analyticsservice.model.dto.response.CustomerStatsResponse;
import com.ticketsupport.analyticsservice.model.dto.response.PriorityCountResponse;
import com.ticketsupport.analyticsservice.model.dto.response.StatusCountResponse;
import com.ticketsupport.analyticsservice.model.dto.response.SummaryResponse;
import com.ticketsupport.analyticsservice.model.dto.response.TrendPointResponse;

import java.util.List;

public interface AnalyticsService {
    SummaryResponse summary(String authHeader);

    List<StatusCountResponse> byStatus(String authHeader);

    List<PriorityCountResponse> byPriority(String authHeader);

    List<CategoryCountResponse> byCategory(String authHeader);

    List<TrendPointResponse> trend(String period, String authHeader);

    List<AgentPerformanceResponse> agentPerformance(String role, String authHeader);

    CustomerStatsResponse customerStats(Long userId, String authHeader);
}
