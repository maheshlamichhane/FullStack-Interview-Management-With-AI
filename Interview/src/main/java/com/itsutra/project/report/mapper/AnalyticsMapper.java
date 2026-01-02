package com.itsutra.project.report.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsutra.project.report.dto.*;
import com.itsutra.project.report.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsMapper {

    private final ObjectMapper objectMapper;

    // Report Mappings
    public Report toReportEntity(ReportRequestDTO request) {
        return Report.builder()
                .name(request.getName())
                .description(request.getDescription())
                .code(request.getCode())
                .reportType(request.getReportType())
                .category(request.getCategory())
                .sqlQuery(request.getSqlQuery())
                .dataSource(request.getDataSource())
                .parameters(convertToReportParameters(request.getParameters()))
                .columns(convertToReportColumns(request.getColumns()))
                .refreshFrequency(request.getRefreshFrequency())
                .isScheduled(request.getIsScheduled() != null ? request.getIsScheduled() : false)
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
                .cacheDuration(request.getCacheDuration() != null ? request.getCacheDuration() : 30)
                .isActive(true)
                .build();
    }

    public ReportResponseDTO toReportResponse(Report entity, Long executionCount, Double averageExecutionTime) {
        ReportResponseDTO response = new ReportResponseDTO();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setCode(entity.getCode());
        response.setReportType(entity.getReportType());
        response.setCategory(entity.getCategory());
        response.setSqlQuery(entity.getSqlQuery());
        response.setDataSource(entity.getDataSource());
        response.setParameters(convertToReportParameterResponses(entity.getParameters()));
        response.setColumns(convertToReportColumnResponses(entity.getColumns()));
        response.setRefreshFrequency(entity.getRefreshFrequency());
        response.setIsScheduled(entity.getIsScheduled());
        response.setIsPublic(entity.getIsPublic());
        response.setIsActive(entity.getIsActive());
        response.setCacheDuration(entity.getCacheDuration());
        response.setLastRunAt(entity.getLastRunAt());
        response.setNextRunAt(entity.getNextRunAt());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setExecutionCount(executionCount);
        response.setAverageExecutionTime(averageExecutionTime);

        // Set created by info if available
        if (entity.getCreatedBy() != null) {
            UserInfoDTO userInfo = new UserInfoDTO();
            userInfo.setId(entity.getCreatedBy().getId());
            userInfo.setName(entity.getCreatedBy().getDisplayName());
            userInfo.setEmail(entity.getCreatedBy().getEmail());
            response.setCreatedBy(userInfo);
        }

        return response;
    }

    // Dashboard Mappings
    public Dashboard toDashboardEntity(DashboardRequestDTO request) {
        return Dashboard.builder()
                .name(request.getName())
                .description(request.getDescription())
                .code(request.getCode())
                .category(request.getCategory())
                .layoutConfig(convertToJson(request.getLayoutConfig()))
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
                .refreshInterval(request.getRefreshInterval() != null ? request.getRefreshInterval() : 15)
                .isActive(true)
                .build();
    }

    public DashboardResponseDTO toDashboardResponse(Dashboard entity) {
        DashboardResponseDTO response = new DashboardResponseDTO();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setCode(entity.getCode());
        response.setCategory(entity.getCategory());
        response.setLayoutConfig(convertFromJson(entity.getLayoutConfig(), Map.class));
        response.setIsPublic(entity.getIsPublic());
        response.setIsActive(entity.getIsActive());
        response.setRefreshInterval(entity.getRefreshInterval());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getCreatedBy() != null) {
            UserInfoDTO userInfo = new UserInfoDTO();
            userInfo.setId(entity.getCreatedBy().getId());
            userInfo.setName(entity.getCreatedBy().getDisplayName());
            userInfo.setEmail(entity.getCreatedBy().getEmail());
            response.setCreatedBy(userInfo);
        }

        if (entity.getWidgets() != null) {
            response.setWidgets(entity.getWidgets().stream()
                    .map(this::toDashboardWidgetResponse)
                    .collect(Collectors.toList()));
        }

        if (entity.getShares() != null) {
            response.setShares(entity.getShares().stream()
                    .map(this::toDashboardShareResponse)
                    .collect(Collectors.toList()));
        }

        if (entity.getReports() != null) {
            response.setReportCount(entity.getReports().size());
        }

        return response;
    }

    // Metric Mappings
    public Metric toMetricEntity(MetricRequestDTO request) {
        return Metric.builder()
                .name(request.getName())
                .description(request.getDescription())
                .code(request.getCode())
                .category(request.getCategory())
                .dataType(request.getDataType())
                .calculationFormula(request.getCalculationFormula())
                .dataSource(request.getDataSource())
                .aggregationType(request.getAggregationType())
                .unit(request.getUnit())
                .targetValue(request.getTargetValue())
                .warningThreshold(request.getWarningThreshold())
                .criticalThreshold(request.getCriticalThreshold())
                .isTrendAvailable(request.getIsTrendAvailable() != null ? request.getIsTrendAvailable() : false)
                .isActive(true)
                .build();
    }

    public MetricResponseDTO toMetricResponse(Metric entity, MetricValue currentValue, List<MetricValue> historicalValues) {
        MetricResponseDTO response = new MetricResponseDTO();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setCode(entity.getCode());
        response.setCategory(entity.getCategory());
        response.setDataType(entity.getDataType());
        response.setCalculationFormula(entity.getCalculationFormula());
        response.setDataSource(entity.getDataSource());
        response.setAggregationType(entity.getAggregationType());
        response.setUnit(entity.getUnit());
        response.setTargetValue(entity.getTargetValue());
        response.setWarningThreshold(entity.getWarningThreshold());
        response.setCriticalThreshold(entity.getCriticalThreshold());
        response.setIsTrendAvailable(entity.getIsTrendAvailable());
        response.setTrendDirection(entity.getTrendDirection());
        response.setIsActive(entity.getIsActive());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        if (currentValue != null) {
            response.setCurrentValue(toMetricValueResponse(currentValue));
        }

        if (historicalValues != null) {
            response.setHistoricalValues(historicalValues.stream()
                    .map(this::toMetricValueResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    // Visualization Mappings
    public Visualization toVisualizationEntity(VisualizationRequestDTO request) {
        return Visualization.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .config(convertToJson(request.getConfig()))
                .dataQuery(request.getDataQuery())
                .width(request.getWidth() != null ? request.getWidth() : 400)
                .height(request.getHeight() != null ? request.getHeight() : 300)
                .isInteractive(request.getIsInteractive() != null ? request.getIsInteractive() : false)
                .refreshInterval(request.getRefreshInterval())
                .build();
    }

    public VisualizationResponseDTO toVisualizationResponse(Visualization entity) {
        VisualizationResponseDTO response = new VisualizationResponseDTO();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setType(entity.getType());
        response.setConfig(convertFromJson(entity.getConfig(), Map.class));
        response.setDataQuery(entity.getDataQuery());
        response.setWidth(entity.getWidth());
        response.setHeight(entity.getHeight());
        response.setIsInteractive(entity.getIsInteractive());
        response.setRefreshInterval(entity.getRefreshInterval());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getReport() != null) {
            ReportSummaryResponseDTO reportSummary = new ReportSummaryResponseDTO();
            reportSummary.setId(entity.getReport().getId());
            reportSummary.setName(entity.getReport().getName());
            reportSummary.setCode(entity.getReport().getCode());
            reportSummary.setCategory(entity.getReport().getCategory());
            response.setReport(reportSummary);
        }

        if (entity.getDashboard() != null) {
            DashboardSummaryResponseDTO dashboardSummary = new DashboardSummaryResponseDTO();
            dashboardSummary.setId(entity.getDashboard().getId());
            dashboardSummary.setName(entity.getDashboard().getName());
            dashboardSummary.setCode(entity.getDashboard().getCode());
            dashboardSummary.setCategory(entity.getDashboard().getCategory());
            response.setDashboard(dashboardSummary);
        }

        return response;
    }

    // Supporting entity mappings
    public DashboardWidgetResponseDTO toDashboardWidgetResponse(DashboardWidget entity) {
        DashboardWidgetResponseDTO response = new DashboardWidgetResponseDTO();
        response.setId(entity.getId());
        response.setWidgetType(entity.getWidgetType());
        response.setTitle(entity.getTitle());
        response.setPositionX(entity.getPositionX());
        response.setPositionY(entity.getPositionY());
        response.setWidth(entity.getWidth());
        response.setHeight(entity.getHeight());
        response.setConfig(convertFromJson(entity.getConfig(), Map.class));

        if (entity.getVisualization() != null) {
            response.setVisualization(toVisualizationResponse(entity.getVisualization()));
        }

        if (entity.getMetric() != null) {
            response.setMetric(toMetricResponse(entity.getMetric(), null, null));
        }

        return response;
    }

    public DashboardShareResponseDTO toDashboardShareResponse(DashboardShare entity) {
        DashboardShareResponseDTO response = new DashboardShareResponseDTO();
        response.setId(entity.getId());
        response.setDashboardId(entity.getDashboard().getId());
        response.setSharedWithUserId(entity.getSharedWithUserId());
        response.setSharedWithRole(entity.getSharedWithRole());
        response.setPermissionLevel(entity.getPermissionLevel());
        response.setExpiresAt(entity.getExpiresAt());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }

    public MetricValueResponseDTO toMetricValueResponse(MetricValue entity) {
        MetricValueResponseDTO response = new MetricValueResponseDTO();
        response.setId(entity.getId());
        response.setMetricId(entity.getMetric().getId());
        response.setValue(entity.getValue());
        response.setCalculatedAt(entity.getCalculatedAt());
        response.setTimePeriod(entity.getTimePeriod());
        response.setDimensionFilters(convertFromJson(entity.getDimensionFilters(), Map.class));
        response.setPreviousValue(entity.getPreviousValue());
        response.setChangePercentage(entity.getChangePercentage());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }

    // Helper methods for JSON conversion
    public String convertToJson(Object object) {
        try {
            return object != null ? objectMapper.writeValueAsString(object) : null;
        } catch (Exception e) {
            log.error("Error converting object to JSON", e);
            return null;
        }
    }

    public <T> T convertFromJson(String json, Class<T> type) {
        try {
            return json != null ? objectMapper.readValue(json, type) : null;
        } catch (Exception e) {
            log.error("Error converting JSON to object", e);
            return null;
        }
    }

    public List<ReportParameterDTO> convertToReportParameters(List<ReportParameterRequestDTO> parameters) {
        if (parameters == null) return null;
        return parameters.stream()
                .map(param -> ReportParameterDTO.builder()
                        .name(param.getName())
                        .type(param.getType())
                        .defaultValue(param.getDefaultValue())
                        .required(param.getRequired() != null ? param.getRequired() : false)
                        .options(param.getOptions() != null ? convertToJson(param.getOptions()) : null)
                        .build())
                .collect(Collectors.toList());
    }

    private List<ReportParameterResponseDTO> convertToReportParameterResponses(List<ReportParameterDTO> parameters) {
        if (parameters == null) return null;
        return parameters.stream()
                .map(param -> {
                    ReportParameterResponseDTO response = new ReportParameterResponseDTO();
                    response.setName(param.getName());
                    response.setType(param.getType());
                    response.setDefaultValue(param.getDefaultValue());
                    response.setRequired(param.getRequired());
                    response.setOptions(convertFromJson(param.getOptions(), List.class));
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<ReportColumnDTO> convertToReportColumns(List<ReportColumnRequestDTO> columns) {
        if (columns == null) return null;
        return columns.stream()
                .map(col -> ReportColumnDTO.builder()
                        .name(col.getName())
                        .dataType(col.getDataType())
                        .displayName(col.getDisplayName())
                        .sortable(col.getSortable() != null ? col.getSortable() : false)
                        .filterable(col.getFilterable() != null ? col.getFilterable() : false)
                        .format(col.getFormat())
                        .build())
                .collect(Collectors.toList());
    }

    private List<ReportColumnResponseDTO> convertToReportColumnResponses(List<ReportColumnDTO> columns) {
        if (columns == null) return null;
        return columns.stream()
                .map(col -> {
                    ReportColumnResponseDTO response = new ReportColumnResponseDTO();
                    response.setName(col.getName());
                    response.setDataType(col.getDataType());
                    response.setDisplayName(col.getDisplayName());
                    response.setSortable(col.getSortable());
                    response.setFilterable(col.getFilterable());
                    response.setFormat(col.getFormat());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
