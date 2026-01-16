package com.core.project.report.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AnalyticsOverviewResponseDTO {
    private Long totalReports;
    private Long totalDashboards;
    private Long totalMetrics;
    private Long scheduledReports;
    private Long publicDashboards;
    private Long activePositions; // New field
    private Map<String, Long> reportsByCategory;
    private Map<String, Long> metricsByCategory;
    private Long totalExecutionsToday;
    private Double averageExecutionTime;

    // Additional analytics fields
    private List<Map<String, Object>> popularReports;
    private List<Map<String, Object>> recentActivities;
    private String systemHealth;
}
