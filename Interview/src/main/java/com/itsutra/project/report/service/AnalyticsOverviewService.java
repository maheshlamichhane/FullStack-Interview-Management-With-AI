package com.itsutra.project.report.service;

import com.itsutra.project.report.dao.*;
import com.itsutra.project.report.dto.AnalyticsOverviewResponseDTO;
import com.itsutra.project.report.entity.Report;
import com.itsutra.project.report.entity.ReportExecution;
import com.itsutra.project.report.enums.MetricCategory;
import com.itsutra.project.report.enums.ReportCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AnalyticsOverviewService {

    private final ReportDAO reportDAO;
    private final DashboardDAO dashboardDAO;
    private final MetricDAO metricDAO;
    private final ReportExecutionDAO reportExecutionDAO;
    private final UserDAO userDAO;
    private final VisualizationDAO visualizationDAO;

    /**
     * Get comprehensive analytics overview for the entire system
     */
    public AnalyticsOverviewResponseDTO getAnalyticsOverview() {
        log.debug("Generating comprehensive analytics overview");

        AnalyticsOverviewResponseDTO overview = new AnalyticsOverviewResponseDTO();

        // Basic counts
        overview.setTotalReports(reportDAO.count());
        overview.setTotalDashboards(dashboardDAO.count());
        overview.setTotalMetrics(metricDAO.count());

        // Active and scheduled counts
        overview.setActivePositions(calculateActivePositions());
        overview.setScheduledReports(getScheduledReportsCount());
        overview.setPublicDashboards(getPublicDashboardsCount());

        // Category distributions
        overview.setReportsByCategory(getReportsByCategory());
        overview.setMetricsByCategory(getMetricsByCategory());

        // Today's execution stats
        overview.setTotalExecutionsToday(getTodayExecutionsCount());
        overview.setAverageExecutionTime(getAverageExecutionTime());

        // Additional analytics data
        overview.setPopularReports(getPopularReports());
        overview.setRecentActivities(getRecentActivities());
        overview.setSystemHealth(getSystemHealthStatus());

        return overview;
    }

    /**
     * Get detailed analytics statistics with breakdowns
     */
    public Map<String, Object> getAnalyticsStats() {
        log.debug("Generating detailed analytics statistics");

        Map<String, Object> stats = new HashMap<>();

        // Report statistics
        stats.put("reports", getReportStatistics());

        // Dashboard statistics
        stats.put("dashboards", getDashboardStatistics());

        // Metric statistics
        stats.put("metrics", getMetricStatistics());

        // Execution statistics
        stats.put("executions", getExecutionStatistics());

        // User statistics
        stats.put("users", getUserStatistics());

        // Performance statistics
        stats.put("performance", getPerformanceStatistics());

        // Recent activity
        stats.put("recentActivity", getRecentActivityStats());

        stats.put("generatedAt", LocalDateTime.now());

        return stats;
    }

    /**
     * Get analytics overview for a specific user
     */
    public Map<String, Object> getUserAnalyticsOverview(Long userId) {
        log.debug("Generating analytics overview for user: {}", userId);

        Map<String, Object> userOverview = new HashMap<>();

        // User-specific report counts
        Long userReportsCount = reportDAO.findAll().stream()
                .filter(report -> report.getCreatedBy() != null && report.getCreatedBy().getId().equals(userId))
                .count();

        Long userDashboardsCount = dashboardDAO.findAll().stream()
                .filter(dashboard -> dashboard.getCreatedBy() != null && dashboard.getCreatedBy().getId().equals(userId))
                .count();

        // Recent user activity
        List<ReportExecution> userExecutions = reportExecutionDAO.findAll().stream()
                .filter(execution -> execution.getReport().getCreatedBy() != null &&
                        execution.getReport().getCreatedBy().getId().equals(userId))
                .sorted((e1, e2) -> e2.getExecutedAt().compareTo(e1.getExecutedAt()))
                .limit(10)
                .collect(Collectors.toList());

        userOverview.put("userReportsCount", userReportsCount);
        userOverview.put("userDashboardsCount", userDashboardsCount);
        userOverview.put("recentExecutions", userExecutions.size());
        userOverview.put("averageExecutionTime", calculateUserAverageExecutionTime(userId));
        userOverview.put("favoriteReports", getUserFavoriteReports(userId));

        return userOverview;
    }

    /**
     * Get hiring analytics dashboard data
     */
    public Map<String, Object> getHiringAnalytics() {
        log.debug("Generating hiring analytics data");

        Map<String, Object> hiringAnalytics = new HashMap<>();

        // Key hiring metrics (these would come from your metric service)
        hiringAnalytics.put("timeToHire", calculateAverageTimeToHire());
        hiringAnalytics.put("offerAcceptanceRate", calculateOfferAcceptanceRate());
        hiringAnalytics.put("interviewSuccessRate", calculateInterviewSuccessRate());
        hiringAnalytics.put("costPerHire", calculateCostPerHire());
        hiringAnalytics.put("qualityOfHire", calculateQualityOfHire());

        // Hiring funnel metrics
        hiringAnalytics.put("hiringFunnel", getHiringFunnelData());

        // Source effectiveness
        hiringAnalytics.put("sourceEffectiveness", getSourceEffectivenessData());

        // Diversity metrics
        hiringAnalytics.put("diversityMetrics", getDiversityMetrics());

        // Time-based trends
        hiringAnalytics.put("monthlyHires", getMonthlyHiresData());
        hiringAnalytics.put("departmentBreakdown", getDepartmentHiringData());

        return hiringAnalytics;
    }

    /**
     * Get system health status for analytics service
     */
    public Map<String, Object> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();

        // Database connectivity check
        health.put("database", checkDatabaseHealth());

        // Scheduled job status
        health.put("scheduledJobs", checkScheduledJobsHealth());

        // Memory usage
        health.put("memory", getMemoryUsage());

        // Service dependencies
        health.put("dependencies", checkServiceDependencies());

        // Overall status
        health.put("status", determineOverallHealth(health));
        health.put("lastChecked", LocalDateTime.now());

        return health;
    }

    // Private helper methods

    private Long calculateActivePositions() {
        // This would integrate with Job Service
        // For now, return a mock value
        return 15L;
    }

    private Long getScheduledReportsCount() {
        return reportDAO.findByIsScheduled(true, Pageable.unpaged()).getTotalElements();
    }

    private Long getPublicDashboardsCount() {
        return dashboardDAO.findByIsPublic(true, Pageable.unpaged()).getTotalElements();
    }

    private Map<String, Long> getReportsByCategory() {
        return reportDAO.countReportsByCategory().stream()
                .collect(Collectors.toMap(
                        arr -> ((ReportCategory) arr[0]).name(),
                        arr -> (Long) arr[1]
                ));
    }

    private Map<String, Long> getMetricsByCategory() {
        return metricDAO.countMetricsByCategory().stream()
                .collect(Collectors.toMap(
                        arr -> ((MetricCategory) arr[0]).name(),
                        arr -> (Long) arr[1]
                ));
    }

    private Long getTodayExecutionsCount() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return reportExecutionDAO.findExecutionsByReportAndDateRange(null, startOfDay, LocalDateTime.now())
                .stream()
                .distinct()
                .count();
    }

    private Double getAverageExecutionTime() {
        return reportDAO.findAll().stream()
                .mapToDouble(report -> {
                    Double avgTime = reportExecutionDAO.findAverageExecutionTimeByReport(report.getId());
                    return avgTime != null ? avgTime : 0.0;
                })
                .average()
                .orElse(0.0);
    }

    private List<Map<String, Object>> getPopularReports() {
        return reportDAO.findAll().stream()
                .sorted((r1, r2) -> {
                    Long e1 = reportExecutionDAO.countByReportId(r1.getId());
                    Long e2 = reportExecutionDAO.countByReportId(r2.getId());
                    return e2.compareTo(e1);
                })
                .limit(5)
                .map(report -> {
                    Map<String, Object> popularReport = new HashMap<>();
                    popularReport.put("id", report.getId());
                    popularReport.put("name", report.getName());
                    popularReport.put("category", report.getCategory());
                    popularReport.put("executionCount", reportExecutionDAO.countByReportId(report.getId()));
                    return popularReport;
                })
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> getRecentActivities() {
        return reportExecutionDAO.findAll().stream()
                .sorted((e1, e2) -> e2.getExecutedAt().compareTo(e1.getExecutedAt()))
                .limit(10)
                .map(execution -> {
                    Map<String, Object> activity = new HashMap<>();
                    activity.put("id", execution.getId());
                    activity.put("reportName", execution.getReport().getName());
                    activity.put("executedAt", execution.getExecutedAt());
                    activity.put("status", execution.getStatus());
                    activity.put("executionTime", execution.getExecutionTimeMs());
                    return activity;
                })
                .collect(Collectors.toList());
    }

    private String getSystemHealthStatus() {
        // Simple health check - in production, this would check various system components
        return "HEALTHY";
    }

    private Map<String, Object> getReportStatistics() {
        Map<String, Object> reportStats = new HashMap<>();
        reportStats.put("total", reportDAO.count());
        reportStats.put("active", reportDAO.findByIsActive(true, Pageable.unpaged()).getTotalElements());
        reportStats.put("scheduled", getScheduledReportsCount());
        reportStats.put("public", reportDAO.findByIsPublic(true, Pageable.unpaged()).getTotalElements());

        // Report execution success rate
        Long totalExecutions = reportExecutionDAO.count();
        int successfulExecutions = reportExecutionDAO.findByStatus(ReportExecution.ExecutionStatus.COMPLETED).size();
        double successRate = totalExecutions > 0 ? (successfulExecutions / totalExecutions.doubleValue()) * 100 : 0;
        reportStats.put("successRate", Math.round(successRate * 100.0) / 100.0);

        return reportStats;
    }

    private Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboardStats = new HashMap<>();
        dashboardStats.put("total", dashboardDAO.count());
        dashboardStats.put("active", dashboardDAO.findByIsActive(true, Pageable.unpaged()).getTotalElements());
        dashboardStats.put("public", getPublicDashboardsCount());

        // Dashboard widget statistics
        Long totalWidgets = dashboardDAO.findAll().stream()
                .mapToLong(dashboard -> dashboard.getWidgets().size())
                .sum();
        dashboardStats.put("totalWidgets", totalWidgets);

        // Average widgets per dashboard
        double avgWidgets = dashboardDAO.count() > 0 ? (double) totalWidgets / dashboardDAO.count() : 0;
        dashboardStats.put("avgWidgetsPerDashboard", Math.round(avgWidgets * 100.0) / 100.0);

        return dashboardStats;
    }

    private Map<String, Object> getMetricStatistics() {
        Map<String, Object> metricStats = new HashMap<>();
        metricStats.put("total", metricDAO.count());
        metricStats.put("active", metricDAO.findByIsActive(true, Pageable.unpaged()).getTotalElements());
        metricStats.put("withTrends", metricDAO.findMetricsWithTrends().size());

        // Metric value statistics
        Long totalMetricValues = metricDAO.findAll().stream()
                .mapToLong(metric -> metric.getValues().size())
                .sum();
        metricStats.put("totalValues", totalMetricValues);

        return metricStats;
    }

    private Map<String, Object> getExecutionStatistics() {
        Map<String, Object> executionStats = new HashMap<>();

        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        LocalDateTime last7Days = LocalDateTime.now().minusDays(7);
        LocalDateTime last30Days = LocalDateTime.now().minusDays(30);

        executionStats.put("last24h", getExecutionCountSince(last24Hours));
        executionStats.put("last7d", getExecutionCountSince(last7Days));
        executionStats.put("last30d", getExecutionCountSince(last30Days));

        // Execution status breakdown
        Map<String, Long> statusBreakdown = reportExecutionDAO.findAll().stream()
                .collect(Collectors.groupingBy(
                        execution -> execution.getStatus().name(),
                        Collectors.counting()
                ));
        executionStats.put("statusBreakdown", statusBreakdown);

        return executionStats;
    }

    private Map<String, Object> getUserStatistics() {
        Map<String, Object> userStats = new HashMap<>();
        userStats.put("total", userDAO.count());
//        userStats.put("active", userDAO.countByActive());

        // User role distribution
//        Map<String, Long> roleDistribution = userDAO.countByIsActiveTrue().stream()
//                .collect(Collectors.toMap(
//                        arr -> ((UserRole) arr[0]).name(),
//                        arr -> (Long) arr[1]
//                ));
        Map<String, Long> roleDistribution = null;
        userStats.put("roleDistribution", roleDistribution);

        // Recently active users
//        userStats.put("recentlyActive", userDAO.findRecentlyActiveUsers().size());

        return userStats;
    }

    private Map<String, Object> getPerformanceStatistics() {
        Map<String, Object> performanceStats = new HashMap<>();

        // Average execution times by report category
        Map<String, Double> avgExecutionByCategory = new HashMap<>();
        for (ReportCategory category : ReportCategory.values()) {
            Double avgTime = reportDAO.findByCategory(category, Pageable.unpaged()).stream()
                    .mapToDouble(report -> {
                        Double time = reportExecutionDAO.findAverageExecutionTimeByReport(report.getId());
                        return time != null ? time : 0.0;
                    })
                    .average()
                    .orElse(0.0);
            avgExecutionByCategory.put(category.name(), Math.round(avgTime * 100.0) / 100.0);
        }
        performanceStats.put("avgExecutionByCategory", avgExecutionByCategory);

        // Cache hit rates (simplified)
        performanceStats.put("cacheHitRate", 87.5);

        return performanceStats;
    }

    private List<Map<String, Object>> getRecentActivityStats() {
        return reportExecutionDAO.findAll().stream()
                .sorted((e1, e2) -> e2.getExecutedAt().compareTo(e1.getExecutedAt()))
                .limit(20)
                .map(execution -> {
                    Map<String, Object> activity = new HashMap<>();
                    activity.put("type", "REPORT_EXECUTION");
                    activity.put("id", execution.getId());
                    activity.put("reportName", execution.getReport().getName());
                    activity.put("user", execution.getReport().getCreatedBy() != null ?
                            execution.getReport().getCreatedBy().getFullName() : "System");
                    activity.put("timestamp", execution.getExecutedAt());
                    activity.put("status", execution.getStatus());
                    activity.put("duration", execution.getExecutionTimeMs());
                    return activity;
                })
                .collect(Collectors.toList());
    }

    private Long getExecutionCountSince(LocalDateTime since) {
        return reportExecutionDAO.findExecutionsByReportAndDateRange(null, since, LocalDateTime.now())
                .stream()
                .distinct()
                .count();
    }

    private Double calculateUserAverageExecutionTime(Long userId) {
        return reportDAO.findAll().stream()
                .filter(report -> report.getCreatedBy() != null && report.getCreatedBy().getId().equals(userId))
                .mapToDouble(report -> {
                    Double avgTime = reportExecutionDAO.findAverageExecutionTimeByReport(report.getId());
                    return avgTime != null ? avgTime : 0.0;
                })
                .average()
                .orElse(0.0);
    }

    private List<Map<String, Object>> getUserFavoriteReports(Long userId) {
        // This would track user favorites - for now, return most executed reports by user
        return reportDAO.findAll().stream()
                .filter(report -> report.getCreatedBy() != null && report.getCreatedBy().getId().equals(userId))
                .sorted((r1, r2) -> {
                    Long e1 = reportExecutionDAO.countByReportId(r1.getId());
                    Long e2 = reportExecutionDAO.countByReportId(r2.getId());
                    return e2.compareTo(e1);
                })
                .limit(3)
                .map(report -> {
                    Map<String, Object> favorite = new HashMap<>();
                    favorite.put("id", report.getId());
                    favorite.put("name", report.getName());
                    favorite.put("executionCount", reportExecutionDAO.countByReportId(report.getId()));
                    return favorite;
                })
                .collect(Collectors.toList());
    }

    // Hiring Analytics Helper Methods

    private Double calculateAverageTimeToHire() {
        // This would integrate with your candidate service
        // Mock data for demonstration
        return 28.5; // days
    }

    private Double calculateOfferAcceptanceRate() {
        // Mock data
        return 78.2; // percentage
    }

    private Double calculateInterviewSuccessRate() {
        // Mock data
        return 65.8; // percentage
    }

    private Double calculateCostPerHire() {
        // Mock data
        return 4250.0; // currency
    }

    private Double calculateQualityOfHire() {
        // Mock data (scale 1-5)
        return 4.2;
    }

    private Map<String, Object> getHiringFunnelData() {
        // Mock hiring funnel data
        return Map.of(
                "applications", 1250,
                "screening", 450,
                "interviews", 180,
                "offers", 45,
                "hires", 35
        );
    }

    private Map<String, Object> getSourceEffectivenessData() {
        // Mock source effectiveness data
        return Map.of(
                "LinkedIn", Map.of("candidates", 450, "hires", 15, "conversion", 3.3),
                "Indeed", Map.of("candidates", 320, "hires", 8, "conversion", 2.5),
                "Career Site", Map.of("candidates", 280, "hires", 7, "conversion", 2.5),
                "Referral", Map.of("candidates", 150, "hires", 12, "conversion", 8.0),
                "Other", Map.of("candidates", 50, "hires", 3, "conversion", 6.0)
        );
    }

    private Map<String, Object> getDiversityMetrics() {
        // Mock diversity metrics
        return Map.of(
                "gender", Map.of("male", 52.0, "female", 45.0, "other", 3.0),
                "ethnicity", Map.of("asian", 25.0, "white", 45.0, "black", 15.0, "hispanic", 12.0, "other", 3.0),
                "diversityIndex", 68.5
        );
    }

    private Map<String, Object> getMonthlyHiresData() {
        // Mock monthly hires data
        return Map.of(
                "January", 8,
                "February", 12,
                "March", 15,
                "April", 10,
                "May", 18,
                "June", 22
        );
    }

    private Map<String, Object> getDepartmentHiringData() {
        // Mock department hiring data
        return Map.of(
                "Engineering", 25,
                "Sales", 15,
                "Marketing", 8,
                "Product", 12,
                "Operations", 5
        );
    }

    // System Health Helper Methods

    private String checkDatabaseHealth() {
        try {
            reportExecutionDAO.count(); // Simple query to test database connectivity
            return "HEALTHY";
        } catch (Exception e) {
            log.error("Database health check failed", e);
            return "UNHEALTHY";
        }
    }

    private String checkScheduledJobsHealth() {
        // Check if scheduled reports are running
        List<Report> overdueScheduledReports = reportDAO.findScheduledReportsDueForExecution().stream()
                .filter(report -> report.getNextRunAt() != null &&
                        report.getNextRunAt().isBefore(LocalDateTime.now().minusMinutes(30)))
                .collect(Collectors.toList());

        return overdueScheduledReports.isEmpty() ? "HEALTHY" : "DEGRADED";
    }

    private Map<String, Object> getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        double memoryUsagePercent = (double) usedMemory / maxMemory * 100;

        return Map.of(
                "usedMB", Math.round(usedMemory / (1024 * 1024)),
                "maxMB", Math.round(maxMemory / (1024 * 1024)),
                "usagePercent", Math.round(memoryUsagePercent * 100.0) / 100.0,
                "status", memoryUsagePercent > 90 ? "CRITICAL" : memoryUsagePercent > 80 ? "WARNING" : "HEALTHY"
        );
    }

    private Map<String, String> checkServiceDependencies() {
        // Check dependencies on other services
        // This would make actual health checks to dependent services
        return Map.of(
                "candidate-service", "HEALTHY",
                "interview-service", "HEALTHY",
                "job-service", "HEALTHY",
                "user-service", "HEALTHY"
        );
    }

    private String determineOverallHealth(Map<String, Object> healthChecks) {
        // Simple logic to determine overall health
        if ("UNHEALTHY".equals(healthChecks.get("database"))) {
            return "CRITICAL";
        }

        if ("DEGRADED".equals(healthChecks.get("scheduledJobs"))) {
            return "DEGRADED";
        }

        Map<String, Object> memory = (Map<String, Object>) healthChecks.get("memory");
        if ("CRITICAL".equals(memory.get("status"))) {
            return "CRITICAL";
        }

        return "HEALTHY";
    }
}
