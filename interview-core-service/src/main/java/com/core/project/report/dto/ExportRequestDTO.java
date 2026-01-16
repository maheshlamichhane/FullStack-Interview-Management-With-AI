package com.core.project.report.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ExportRequestDTO {
    private String format; // CSV, EXCEL, PDF, JSON
    private Map<String, Object> filters;
    private Boolean includeCharts;
    private String timeRange;
}
