package com.itsutra.project.report.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class MetricValueRequestDTO {
    @NotNull(message = "Metric ID is required")
    private Long metricId;

    @NotNull(message = "Value is required")
    private Double value;

    private LocalDateTime calculatedAt;
    private String timePeriod;
    private Map<String, Object> dimensionFilters;
}
