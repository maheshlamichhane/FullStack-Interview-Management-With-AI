package com.interview.project.report.dto;


import com.interview.project.report.enums.DataType;
import com.interview.project.report.enums.MetricCategory;
import com.interview.project.report.enums.TrendDirection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MetricResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String code;
    private MetricCategory category;
    private DataType dataType;
    private String calculationFormula;
    private String dataSource;
    private String aggregationType;
    private String unit;
    private Double targetValue;
    private Double warningThreshold;
    private Double criticalThreshold;
    private Boolean isTrendAvailable;
    private TrendDirection trendDirection;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private MetricValueResponseDTO currentValue;
    private List<MetricValueResponseDTO> historicalValues;
}
