package com.itsutra.project.service;

import com.itsutra.project.dao.DashboardDAO;
import com.itsutra.project.dao.MetricDAO;
import com.itsutra.project.dao.ReportDAO;
import com.itsutra.project.dao.VisualizationDAO;
import com.itsutra.project.dto.*;
import com.itsutra.project.entity.*;
import com.itsutra.project.mapper.AnalyticsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final DashboardDAO dashboardDAO;
    private final ReportDAO reportDAO;
    private final VisualizationDAO visualizationDAO;
    private final MetricDAO metricDAO;
    private final AnalyticsMapper analyticsMapper;
    private final AuthenticationService authenticationService;

    @Transactional
    public DashboardResponseDTO createDashboard(DashboardRequestDTO request) {
        log.info("Creating new dashboard with code: {}", request.getCode());

        User user = authenticationService.getCurrentUser();
        if (dashboardDAO.existsByCodeAndCreatedById(request.getCode(),user.getId())) {
            throw new IllegalArgumentException("Dashboard code already exists: " + request.getCode());
        }

        Dashboard dashboard = analyticsMapper.toDashboardEntity(request);
        dashboard.setCreatedBy(user);

        // Add widgets if provided
        if (request.getWidgets() != null && !request.getWidgets().isEmpty()) {
            List<DashboardWidget> widgets = createDashboardWidgets(dashboard, request.getWidgets());
            dashboard.setWidgets(widgets);
        }
        dashboard = dashboardDAO.save(dashboard);

        log.info("Successfully created dashboard with id: {}", dashboard.getId());
        return analyticsMapper.toDashboardResponse(dashboard);
    }

    @Transactional(readOnly = true)
    public List<DashboardResponseDTO> getAllDashboards() {
        User user = authenticationService.getCurrentUser();
        log.debug("Fetching all dashboards");
        return dashboardDAO.findDashboardsByCreator(user.getId()).stream()
                .map(analyticsMapper::toDashboardResponse)
                .collect(Collectors.toList());
    }





    @Transactional(readOnly = true)
    public DashboardResponseDTO getDashboardById(Long id) {
        log.debug("Fetching dashboard by id: {}", id);
        User user = authenticationService.getCurrentUser();
        Dashboard dashboard = dashboardDAO.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Dashboard not found with id: " + id));
        return analyticsMapper.toDashboardResponse(dashboard);
    }


    @Transactional
    public DashboardResponseDTO updateDashboard(Long id, DashboardRequestDTO request) {
        log.info("Updating dashboard with id: {}", id);
        User user = authenticationService.getCurrentUser();
        Dashboard dashboard = dashboardDAO.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Dashboard not found with id: " + id));


        // Validate code uniqueness if changed
        if (request.getCode() != null && !request.getCode().equals(dashboard.getCode())) {
            if (dashboardDAO.existsByCodeAndCreatedById(request.getCode(),user.getId())) {
                throw new IllegalArgumentException("Dashboard code already exists: " + request.getCode());
            }
            dashboard.setCode(request.getCode());
        }

        // Update fields
        Optional.ofNullable(request.getName()).ifPresent(dashboard::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(dashboard::setDescription);
        Optional.ofNullable(request.getCategory()).ifPresent(dashboard::setCategory);
        Optional.ofNullable(request.getIsPublic()).ifPresent(dashboard::setIsPublic);
        Optional.ofNullable(request.getRefreshInterval()).ifPresent(dashboard::setRefreshInterval);

        if (request.getLayoutConfig() != null) {
            dashboard.setLayoutConfig(analyticsMapper.convertToJson(request.getLayoutConfig()));
        }

        Dashboard updatedDashboard = dashboardDAO.save(dashboard);
        log.info("Successfully updated dashboard with id: {}", id);
        return analyticsMapper.toDashboardResponse(updatedDashboard);
    }


    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardData(Long dashboardId) {
        log.debug("Fetching data for dashboard with id: {}", dashboardId);
        User user = authenticationService.getCurrentUser();
        Dashboard dashboard = dashboardDAO.findByIdAndCreatedById(dashboardId,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Dashboard not found with id: " + dashboardId));

        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("dashboard", analyticsMapper.toDashboardResponse(dashboard));


        // Fetch data for each widget
        List<Map<String, Object>> widgetData = new ArrayList<>();
        for (DashboardWidget widget : dashboard.getWidgets()) {
            Map<String, Object> widgetInfo = new HashMap<>();
            widgetInfo.put("widget", analyticsMapper.toDashboardWidgetResponse(widget));

            // Fetch data based on widget type
            if (widget.getVisualization() != null) {
                VisualizationDataResponseDTO vizData = getVisualizationData(widget.getVisualization());
                widgetInfo.put("data", vizData);
            } else if (widget.getMetric() != null) {
                MetricTrendResponseDTO metricData = getMetricTrendData(widget.getMetric());
                widgetInfo.put("data", metricData);
            }

            widgetData.add(widgetInfo);
        }

        dashboardData.put("widgets", widgetData);
        return dashboardData;
    }


    @Transactional
    public DashboardWidgetResponseDTO addWidgetToDashboard(Long dashboardId, DashboardWidgetRequestDTO request) {
        log.info("Adding widget to dashboard with id: {}", dashboardId);

        User user = authenticationService.getCurrentUser();
        Dashboard dashboard = dashboardDAO.findByIdAndCreatedById(dashboardId,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Dashboard not found with id: " + dashboardId));

        DashboardWidget widget = createDashboardWidget(dashboard, request);
        dashboard.getWidgets().add(widget);
        dashboardDAO.save(dashboard);

        log.info("Successfully added widget to dashboard with id: {}", dashboardId);
        return analyticsMapper.toDashboardWidgetResponse(widget);
    }
//
//    public void deleteDashboard(Long id) {
//        log.info("Deleting dashboard with id: {}", id);
//        Dashboard dashboard = dashboardDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Dashboard not found with id: " + id));
//        dashboardDAO.delete(dashboard);
//        log.info("Successfully deleted dashboard with id: {}", id);
//    }
//
//    // Widget Management

//
//    public void removeWidgetFromDashboard(Long dashboardId, Long widgetId) {
//        log.info("Removing widget {} from dashboard {}", widgetId, dashboardId);
//
//        Dashboard dashboard = dashboardDAO.findById(dashboardId)
//                .orElseThrow(() -> new IllegalArgumentException("Dashboard not found with id: " + dashboardId));
//
//        boolean removed = dashboard.getWidgets().removeIf(widget -> widget.getId().equals(widgetId));
//        if (!removed) {
//            throw new IllegalArgumentException("Widget not found with id: " + widgetId);
//        }
//
//        dashboardDAO.save(dashboard);
//        log.info("Successfully removed widget from dashboard");
//    }
//
//    // Dashboard Sharing
//    public DashboardShareResponseDTO shareDashboard(Long dashboardId, DashboardShareRequestDTO request) {
//        log.info("Sharing dashboard {} with user/role", dashboardId);
//
//        Dashboard dashboard = dashboardDAO.findById(dashboardId)
//                .orElseThrow(() -> new IllegalArgumentException("Dashboard not found with id: " + dashboardId));
//
//        DashboardShare share = DashboardShare.builder()
//                .dashboard(dashboard)
//                .sharedWithUserId(request.getSharedWithUserId())
//                .sharedWithRole(request.getSharedWithRole())
//                .permissionLevel(request.getPermissionLevel())
//                .expiresAt(request.getExpiresAt())
//                .build();
//
//        dashboard.getShares().add(share);
//        dashboardDAO.save(dashboard);
//
//        log.info("Successfully shared dashboard with id: {}", dashboardId);
//        return analyticsMapper.toDashboardShareResponse(share);
//    }
//
//    public void revokeDashboardShare(Long dashboardId, Long shareId) {
//        log.info("Revoking share {} from dashboard {}", shareId, dashboardId);
//
//        Dashboard dashboard = dashboardDAO.findById(dashboardId)
//                .orElseThrow(() -> new IllegalArgumentException("Dashboard not found with id: " + dashboardId));
//
//        boolean removed = dashboard.getShares().removeIf(share -> share.getId().equals(shareId));
//        if (!removed) {
//            throw new IllegalArgumentException("Share not found with id: " + shareId);
//        }
//
//        dashboardDAO.save(dashboard);
//        log.info("Successfully revoked dashboard share");
//    }
//
//    // Dashboard Data

//
    // Helper methods
    private List<DashboardWidget> createDashboardWidgets(Dashboard dashboard, List<DashboardWidgetRequestDTO> widgetRequests) {
        return widgetRequests.stream()
                .map(request -> createDashboardWidget(dashboard, request))
                .collect(Collectors.toList());
    }

    private DashboardWidget createDashboardWidget(Dashboard dashboard, DashboardWidgetRequestDTO request) {

        User user = authenticationService.getCurrentUser();
        DashboardWidget widget = new DashboardWidget();
        widget.setDashboard(dashboard);
        widget.setWidgetType(request.getWidgetType());
        widget.setTitle(request.getTitle());
        widget.setPositionX(request.getPositionX());
        widget.setPositionY(request.getPositionY());
        widget.setWidth(request.getWidth());
        widget.setHeight(request.getHeight());
        widget.setConfig(analyticsMapper.convertToJson(request.getConfig()));

//        if (request.getVisualizationId() != null) {
//            Visualization visualization = visualizationDAO.findByIdAndCreatedById(request.getVisualizationId(),dashboard.getCreatedBy().getId())
//                    .orElseThrow(() -> new IllegalArgumentException("Visualization not found with id: " + request.getVisualizationId()));
//            widget.setVisualization(visualization);
//        }

        if (request.getMetricId() != null) {
            Metric metric = metricDAO.findByIdAndCreatedById(request.getMetricId(),user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Metric not found with id: " + request.getMetricId()));
            widget.setMetric(metric);
        }


        return widget;
    }
//
    private VisualizationDataResponseDTO getVisualizationData(Visualization visualization) {
        // Implementation would depend on your data source
        // This is a simplified version
        VisualizationDataResponseDTO response = new VisualizationDataResponseDTO();
        response.setGeneratedAt(LocalDateTime.now());
        // Add actual data fetching logic here
        return response;
    }

    private MetricTrendResponseDTO getMetricTrendData(Metric metric) {
        // Implementation would fetch historical metric values
        MetricTrendResponseDTO response = new MetricTrendResponseDTO();
        response.setMetric(analyticsMapper.toMetricResponse(metric, null, null));
        // Add actual trend calculation logic here
        return response;
    }
}
