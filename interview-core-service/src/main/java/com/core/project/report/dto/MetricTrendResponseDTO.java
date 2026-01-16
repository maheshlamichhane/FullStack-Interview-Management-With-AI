package com.core.project.report.dto;

import lombok.Data;

import java.util.List;

@Data
public class MetricTrendResponseDTO {
    private MetricResponseDTO metric;
    private List<MetricValueResponseDTO> values;
    private Double averageValue;
    private Double minValue;
    private Double maxValue;
    private Double trendPercentage;
}
