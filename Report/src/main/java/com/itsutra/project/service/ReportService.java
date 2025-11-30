package com.itsutra.project.service;

import com.itsutra.project.dao.DashboardDAO;
import com.itsutra.project.dao.ReportDAO;
import com.itsutra.project.dao.ReportExecutionDAO;
import com.itsutra.project.dto.*;
import com.itsutra.project.entity.Dashboard;
import com.itsutra.project.entity.Report;
import com.itsutra.project.entity.ReportExecution;
import com.itsutra.project.mapper.AnalyticsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportDAO reportDAO;
    private final ReportExecutionDAO reportExecutionDAO;
    private final DashboardDAO dashboardDAO;
    private final AnalyticsMapper analyticsMapper;
    private final DataQueryService dataQueryService;
    private final ExportService exportService;

    // Report Management
    public ReportResponseDTO createReport(ReportRequestDTO request) {
        log.info("Creating new report with code: {}", request.getCode());

        if (reportDAO.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Report code already exists: " + request.getCode());
        }

        Report report = analyticsMapper.toReportEntity(request);
        Report savedReport = reportDAO.save(report);

        log.info("Successfully created report with id: {}", savedReport.getId());
        return analyticsMapper.toReportResponse(savedReport, 0L, 0.0);
    }

    @Transactional(readOnly = true)
    public ReportResponseDTO getReportById(Long id) {
        log.debug("Fetching report by id: {}", id);
        Report report = reportDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + id));

        Long executionCount = reportExecutionDAO.countByReportId(id);
        Double averageExecutionTime = reportExecutionDAO.findAverageExecutionTimeByReport(id);

        return analyticsMapper.toReportResponse(report, executionCount, averageExecutionTime);
    }

    @Transactional(readOnly = true)
    public Page<ReportResponseDTO> getAllReports(Pageable pageable) {
        log.debug("Fetching all reports");
        return reportDAO.findAll(pageable)
                .map(report -> {
                    Long executionCount = reportExecutionDAO.countByReportId(report.getId());
                    Double averageExecutionTime = reportExecutionDAO.findAverageExecutionTimeByReport(report.getId());
                    return analyticsMapper.toReportResponse(report, executionCount, averageExecutionTime);
                });
    }

    public ReportResponseDTO updateReport(Long id, ReportRequestDTO request) {
        log.info("Updating report with id: {}", id);

        Report report = reportDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + id));

        // Validate code uniqueness if changed
        if (request.getCode() != null && !request.getCode().equals(report.getCode())) {
            if (reportDAO.existsByCode(request.getCode())) {
                throw new IllegalArgumentException("Report code already exists: " + request.getCode());
            }
            report.setCode(request.getCode());
        }

        // Update fields
        Optional.ofNullable(request.getName()).ifPresent(report::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(report::setDescription);
        Optional.ofNullable(request.getReportType()).ifPresent(report::setReportType);
        Optional.ofNullable(request.getCategory()).ifPresent(report::setCategory);
        Optional.ofNullable(request.getSqlQuery()).ifPresent(report::setSqlQuery);
        Optional.ofNullable(request.getDataSource()).ifPresent(report::setDataSource);
        Optional.ofNullable(request.getRefreshFrequency()).ifPresent(report::setRefreshFrequency);
        Optional.ofNullable(request.getIsScheduled()).ifPresent(report::setIsScheduled);
        Optional.ofNullable(request.getIsPublic()).ifPresent(report::setIsPublic);
        Optional.ofNullable(request.getCacheDuration()).ifPresent(report::setCacheDuration);

        if (request.getParameters() != null) {
            report.setParameters(analyticsMapper.convertToReportParameters(request.getParameters()));
        }

        if (request.getColumns() != null) {
            report.setColumns(analyticsMapper.convertToReportColumns(request.getColumns()));
        }

        Report updatedReport = reportDAO.save(report);
        Long executionCount = reportExecutionDAO.countByReportId(id);
        Double averageExecutionTime = reportExecutionDAO.findAverageExecutionTimeByReport(id);

        log.info("Successfully updated report with id: {}", id);
        return analyticsMapper.toReportResponse(updatedReport, executionCount, averageExecutionTime);
    }

    public void deleteReport(Long id) {
        log.info("Deleting report with id: {}", id);
        Report report = reportDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + id));

        // Check if report is used in any dashboards
        List<Dashboard> dashboardsUsingReport = dashboardDAO.findAll().stream()
                .filter(dashboard -> dashboard.getReports().contains(report))
                .collect(Collectors.toList());

        if (!dashboardsUsingReport.isEmpty()) {
            throw new IllegalStateException("Cannot delete report. It is used in " + dashboardsUsingReport.size() + " dashboard(s).");
        }

        reportDAO.delete(report);
        log.info("Successfully deleted report with id: {}", id);
    }

    // Report Execution
    public ReportExecutionResponseDTO executeReport(Long reportId, ReportExecutionRequestDTO request) {
        log.info("Executing report with id: {}", reportId);

        Report report = reportDAO.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + reportId));

        // Create execution record
        ReportExecution execution = ReportExecution.builder()
                .report(report)
                .executedAt(LocalDateTime.now())
                .status(ReportExecution.ExecutionStatus.RUNNING)
                .parameters(analyticsMapper.convertToJson(request.getParameters()))
                .build();

        ReportExecution savedExecution = reportExecutionDAO.save(execution);

        try {
            // Execute the report query
            ReportDataResponseDTO data = dataQueryService.executeReportQuery(report, request.getParameters());

            // Update execution record
            savedExecution.setStatus(ReportExecution.ExecutionStatus.COMPLETED);
            savedExecution.setCompletedAt(LocalDateTime.now());
            savedExecution.setRecordCount((long) data.getData().size());

            // Generate export if requested
            if (request.getFormat() != null && !request.getFormat().equals("JSON")) {
                String exportUrl = exportService.exportReportData(data, request.getFormat());
                savedExecution.setErrorMessage(exportUrl); // Reusing errorMessage field for export URL
            }

            ReportExecution updatedExecution = reportExecutionDAO.save(savedExecution);

            // Update report last run time
            report.setLastRunAt(LocalDateTime.now());
            if (report.getIsScheduled()) {
                report.setNextRunAt(LocalDateTime.now().plusMinutes(report.getRefreshFrequency()));
            }
            reportDAO.save(report);

            log.info("Successfully executed report with id: {}", reportId);
            return toReportExecutionResponse(updatedExecution, data);

        } catch (Exception e) {
            log.error("Error executing report with id: {}", reportId, e);
            savedExecution.setStatus(ReportExecution.ExecutionStatus.FAILED);
            savedExecution.setErrorMessage(e.getMessage());
            reportExecutionDAO.save(savedExecution);
            throw new RuntimeException("Failed to execute report: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public ReportDataResponseDTO getReportData(Long reportId, Map<String, Object> parameters) {
        log.debug("Fetching report data for report id: {}", reportId);

        Report report = reportDAO.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + reportId));

        return dataQueryService.executeReportQuery(report, parameters);
    }

    // Scheduled Reports
    @Scheduled(fixedRate = 60000) // Run every minute
    public void executeScheduledReports() {
        log.debug("Checking for scheduled reports due for execution");

        List<Report> scheduledReports = reportDAO.findScheduledReportsDueForExecution();

        for (Report report : scheduledReports) {
            try {
                log.info("Executing scheduled report: {}", report.getName());
                ReportExecutionRequestDTO request = new ReportExecutionRequestDTO();
                request.setParameters(Collections.emptyMap());
                request.setFormat("JSON");
                request.setAsync(true);

                executeReport(report.getId(), request);
            } catch (Exception e) {
                log.error("Error executing scheduled report: {}", report.getName(), e);
            }
        }
    }

    // Analytics and Insights
    @Transactional(readOnly = true)
    public Map<String, Object> getReportAnalytics(Long reportId) {
        log.debug("Fetching analytics for report id: {}", reportId);

        Report report = reportDAO.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + reportId));

        Map<String, Object> analytics = new HashMap<>();

        // Execution statistics
        List<Object[]> statusCounts = reportExecutionDAO.countExecutionsByStatus(reportId);
        Double avgExecutionTime = reportExecutionDAO.findAverageExecutionTimeByReport(reportId);

        analytics.put("executionStats", Map.of(
                "statusCounts", statusCounts.stream()
                        .collect(Collectors.toMap(
                                arr -> arr[0].toString(),
                                arr -> arr[1]
                        )),
                "averageExecutionTime", avgExecutionTime != null ? avgExecutionTime : 0
        ));

        // Recent executions
        Page<ReportExecution> recentExecutions = reportExecutionDAO.findByReportId(reportId, Pageable.ofSize(10));
        analytics.put("recentExecutions", recentExecutions.getContent().stream()
                .map(this::toReportExecutionResponse)
                .collect(Collectors.toList()));

        return analytics;
    }

    // Helper methods
    private ReportExecutionResponseDTO toReportExecutionResponse(ReportExecution execution) {
        return toReportExecutionResponse(execution, null);
    }

    private ReportExecutionResponseDTO toReportExecutionResponse(ReportExecution execution, ReportDataResponseDTO data) {
        ReportExecutionResponseDTO response = new ReportExecutionResponseDTO();
        response.setId(execution.getId());
        response.setReportId(execution.getReport().getId());
        response.setExecutedAt(execution.getExecutedAt());
        response.setCompletedAt(execution.getCompletedAt());
        response.setStatus(execution.getStatus());
        response.setExecutionTimeMs(execution.getExecutionTimeMs());
        response.setRecordCount(execution.getRecordCount());
        response.setErrorMessage(execution.getErrorMessage());
        response.setParameters(analyticsMapper.convertFromJson(execution.getParameters(), Map.class));

        if (data != null && execution.getStatus() == ReportExecution.ExecutionStatus.COMPLETED) {
            response.setResultUrl("/api/v1/reports/" + execution.getReport().getId() + "/data?executionId=" + execution.getId());
        }

        return response;
    }
}
