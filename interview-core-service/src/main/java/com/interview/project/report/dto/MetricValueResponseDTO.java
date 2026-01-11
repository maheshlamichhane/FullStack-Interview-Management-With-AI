package com.interview.project.report.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class MetricValueResponseDTO {
    private Long id;
    private Long metricId;
    private Double value;
    private LocalDateTime calculatedAt;
    private String timePeriod;
    private Map<String, Object> dimensionFilters;
    private Double previousValue;
    private Double changePercentage;
    private LocalDateTime createdAt;
}
