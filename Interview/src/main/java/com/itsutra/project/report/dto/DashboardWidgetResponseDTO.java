package com.itsutra.project.report.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DashboardWidgetResponseDTO {
    private Long id;
    private String widgetType;
    private String title;
    private Integer positionX;
    private Integer positionY;
    private Integer width;
    private Integer height;
    private Map<String, Object> config;
    private VisualizationResponseDTO visualization;
    private MetricResponseDTO metric;
}