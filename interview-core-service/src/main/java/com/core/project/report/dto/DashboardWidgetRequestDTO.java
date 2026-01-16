package com.core.project.report.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DashboardWidgetRequestDTO {
    private Long visualizationId;
    private Long metricId;
    private String widgetType;
    private String title;
    private Integer positionX;
    private Integer positionY;
    private Integer width;
    private Integer height;
    private Map<String, Object> config;
}
