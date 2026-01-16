package com.core.project.report.service;//package com.itsutra.project.report.service;
//
//
//import com.itsutra.project.common.entity.User;
//import com.itsutra.project.common.service.AuthenticationService;
//import com.itsutra.project.report.dao.DashboardDAO;
//import com.itsutra.project.report.dao.ReportDAO;
//import com.itsutra.project.report.dao.ReportExecutionDAO;
//import com.itsutra.project.report.dto.*;
//import com.itsutra.project.report.entity.Dashboard;
//import com.itsutra.project.report.entity.Report;
//import com.itsutra.project.report.entity.ReportExecution;
//import com.itsutra.project.report.mapper.AnalyticsMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//@Slf4j
//public class ReportService {
//
//    private final ReportDAO reportDAO;
//    private final ReportExecutionDAO reportExecutionDAO;
//    private final DashboardDAO dashboardDAO;
//    private final AnalyticsMapper analyticsMapper;
//    private final DataQueryService dataQueryService;
//    private final ReportExportMediaterService reportExportMediaterService;
//    private final AuthenticationService authenticationService;
//
//
//
//
//    // Report Management
//    public ReportResponseDTO createReport(ReportRequestDTO request) {
//        log.info("Creating new report with code: {}", request.getCode());
//
//        User currentUser = authenticationService.getCurrentUser();
//        if (reportDAO.existsByCodeAndCreatedById(request.getCode(),currentUser.getId())) {
//            throw new IllegalArgumentException("Report code already exists: " + request.getCode());
//        }
//
//        Report report = analyticsMapper.toReportEntity(request);
//        report.setCreatedBy(currentUser);
//
//        Report savedReport = reportDAO.save(report);
//
//        log.info("Successfully created report with id: {}", savedReport.getId());
//        return analyticsMapper.toReportResponse(savedReport, 0L, 0.0);
//    }
//
//    @Transactional(readOnly = true)
//    public ReportResponseDTO getReportById(Long id) {
//        log.debug("Fetching report by id: {}", id);
//        User currentUser = authenticationService.getCurrentUser();
//        Report report = reportDAO.findByIdAndCreatedById(id,currentUser.getId())
//                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + id));
//
//        Long executionCount = reportExecutionDAO.countByReportId(id);
//        Double averageExecutionTime = reportExecutionDAO.findAverageExecutionTimeByReport(id);
//
//        return analyticsMapper.toReportResponse(report, executionCount, averageExecutionTime);
//    }
//
//
//
//    @Transactional(readOnly = true)
//    public List<ReportResponseDTO> getAllReports() {
//        User currentUser = authenticationService.getCurrentUser();
//        log.debug("Fetching all reports");
//        return reportDAO.findByCreatedById(currentUser.getId()).stream()
//                .map(report -> {
//                    Long executionCount = reportExecutionDAO.countByReportId(report.getId());
//                    Double averageExecutionTime = reportExecutionDAO.findAverageExecutionTimeByReport(report.getId());
//                    return analyticsMapper.toReportResponse(report, executionCount, averageExecutionTime);
//                }).collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public ReportResponseDTO getAllReportsByCode(String code) {
//        User currentUser = authenticationService.getCurrentUser();
//        log.debug("Fetching all reports");
//        Report report = reportDAO.findByCodeAndCreatedById(code,currentUser.getId()).orElseThrow(() ->
//                new IllegalArgumentException("Report not found with code: " + code));
//        Long executionCount = reportExecutionDAO.countByReportId(report.getId());
//        Double averageExecutionTime = reportExecutionDAO.findAverageExecutionTimeByReport(report.getId());
//        return analyticsMapper.toReportResponse(report, executionCount, averageExecutionTime);
//    }
//
//
//    @Transactional
//    public ReportResponseDTO updateReport(Long id, ReportRequestDTO request) {
//        log.info("Updating report with id: {}", id);
//
//        Report report = reportDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + id));
//
//        User currentUser = authenticationService.getCurrentUser();
//        // Validate code uniqueness if changed
//        if (request.getCode() != null && !request.getCode().equals(report.getCode())) {
//            if (reportDAO.existsByCodeAndCreatedById(request.getCode(),currentUser.getId())) {
//                throw new IllegalArgumentException("Report code already exists: " + request.getCode());
//            }
//            report.setCode(request.getCode());
//        }
//
//        // Update fields
//        Optional.ofNullable(request.getName()).ifPresent(report::setName);
//        Optional.ofNullable(request.getDescription()).ifPresent(report::setDescription);
//        Optional.ofNullable(request.getReportType()).ifPresent(report::setReportType);
//        Optional.ofNullable(request.getCategory()).ifPresent(report::setCategory);
//        Optional.ofNullable(request.getSqlQuery()).ifPresent(report::setSqlQuery);
//        Optional.ofNullable(request.getDataSource()).ifPresent(report::setDataSource);
//        Optional.ofNullable(request.getRefreshFrequency()).ifPresent(report::setRefreshFrequency);
//        Optional.ofNullable(request.getIsScheduled()).ifPresent(report::setIsScheduled);
//        Optional.ofNullable(request.getIsPublic()).ifPresent(report::setIsPublic);
//        Optional.ofNullable(request.getCacheDuration()).ifPresent(report::setCacheDuration);
//
//        if (request.getParameters() != null) {
//            report.setParameters(analyticsMapper.convertToReportParameters(request.getParameters()));
//        }
//
//        if (request.getColumns() != null) {
//            report.setColumns(analyticsMapper.convertToReportColumns(request.getColumns()));
//        }
//
//        Report updatedReport = reportDAO.save(report);
//        Long executionCount = reportExecutionDAO.countByReportId(id);
//        Double averageExecutionTime = reportExecutionDAO.findAverageExecutionTimeByReport(id);
//
//        log.info("Successfully updated report with id: {}", id);
//        return analyticsMapper.toReportResponse(updatedReport, executionCount, averageExecutionTime);
//    }
//
//
//
//
//
//
//    @Transactional
//    public ReportExecutionResponseDTO executeReport(Long reportId, ReportExecutionRequestDTO request) {
//        log.info("Executing report with id: {}", reportId);
//
//        Report report = reportDAO.findById(reportId)
//                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + reportId));
//
//        // Create execution record
//        ReportExecution execution = ReportExecution.builder()
//                .report(report)
//                .executedAt(LocalDateTime.now())
//                .status(ReportExecution.ExecutionStatus.RUNNING)
//                .parameters(analyticsMapper.convertToJson(request.getParameters()))
//                .build();
//
//        ReportExecution savedExecution = reportExecutionDAO.save(execution);
//
//        try {
//            // Execute the report query
//            ReportDataResponseDTO data = dataQueryService.executeReportQuery(report, request.getParameters());
//
//            // Update execution record
//            savedExecution.setStatus(ReportExecution.ExecutionStatus.COMPLETED);
//            savedExecution.setCompletedAt(LocalDateTime.now());
//            savedExecution.setRecordCount((long) data.getData().size());
//
//            // Generate export if requested
////                String exportUrl = reportExportMediaterService.exportReportData(data, request.getFormat());
//            String exportUrl;
//
//            ReportExecution updatedExecution = reportExecutionDAO.save(savedExecution);
//
//            // Update report last run time
//            report.setLastRunAt(LocalDateTime.now());
//            if (report.getIsScheduled()) {
//                report.setNextRunAt(LocalDateTime.now().plusMinutes(report.getRefreshFrequency()));
//            }
//            reportDAO.save(report);
//
//            log.info("Successfully executed report with id: {}", reportId);
//            return toReportExecutionResponse(updatedExecution, data);
//
//        } catch (Exception e) {
//            log.error("Error executing report with id: {}", reportId, e);
//            savedExecution.setStatus(ReportExecution.ExecutionStatus.FAILED);
//            savedExecution.setErrorMessage(e.getMessage());
//            reportExecutionDAO.save(savedExecution);
//            throw new RuntimeException("Failed to execute report: " + e.getMessage(), e);
//        }
//    }
//
//        public void deleteReport(Long id) {
//        log.info("Deleting report with id: {}", id);
//        Report report = reportDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + id));
//
//        // Check if report is used in any dashboards
//        List<Dashboard> dashboardsUsingReport = dashboardDAO.findAll().stream()
//                .filter(dashboard -> dashboard.getReports().contains(report))
//                .collect(Collectors.toList());
//
//        if (!dashboardsUsingReport.isEmpty()) {
//            throw new IllegalStateException("Cannot delete report. It is used in " + dashboardsUsingReport.size() + " dashboard(s).");
//        }
//
//        reportDAO.delete(report);
//        log.info("Successfully deleted report with id: {}", id);
//    }
//
//
////
////
////
////    // Scheduled Reports
////    @Scheduled(fixedRate = 60000) // Run every minute
////    public void executeScheduledReports() {
////        log.debug("Checking for scheduled reports due for execution");
////
////        List<Report> scheduledReports = reportDAO.findScheduledReportsDueForExecution();
////
////        for (Report report : scheduledReports) {
////            try {
////                log.info("Executing scheduled report: {}", report.getName());
////                ReportExecutionRequestDTO request = new ReportExecutionRequestDTO();
////                request.setParameters(Collections.emptyMap());
////                request.setFormat("JSON");
////                request.setAsync(true);
////
////                executeReport(report.getId(), request);
////            } catch (Exception e) {
////                log.error("Error executing scheduled report: {}", report.getName(), e);
////            }
////        }
////    }
//
////
////    // Helper methods
//    private ReportExecutionResponseDTO toReportExecutionResponse(ReportExecution execution) {
//        return toReportExecutionResponse(execution, null);
//    }
//
//    private ReportExecutionResponseDTO toReportExecutionResponse(ReportExecution execution, ReportDataResponseDTO data) {
//        ReportExecutionResponseDTO response = new ReportExecutionResponseDTO();
//        response.setId(execution.getId());
//        response.setReportId(execution.getReport().getId());
//        response.setExecutedAt(execution.getExecutedAt());
//        response.setCompletedAt(execution.getCompletedAt());
//        response.setStatus(execution.getStatus());
//        response.setExecutionTimeMs(execution.getExecutionTimeMs());
//        response.setRecordCount(execution.getRecordCount());
//        response.setErrorMessage(execution.getErrorMessage());
//        response.setParameters(analyticsMapper.convertFromJson(execution.getParameters(), Map.class));
//
//        if (data != null && execution.getStatus() == ReportExecution.ExecutionStatus.COMPLETED) {
//            response.setResultUrl("/api/v1/reports/" + execution.getReport().getId() + "/data?executionId=" + execution.getId());
//        }
//
//        return response;
//    }
//
//    //
//
//}
