package com.itsutra.project.dto;

import com.itsutra.project.enums.DataType;
import com.itsutra.project.enums.MetricCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MetricRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Code is required")
    private String code;

    @NotNull(message = "Category is required")
    private MetricCategory category;

    @NotNull(message = "Data type is required")
    private DataType dataType;

    private String calculationFormula;
    private String dataSource;
    private String aggregationType;
    private String unit;
    private Double targetValue;
    private Double warningThreshold;
    private Double criticalThreshold;
    private Boolean isTrendAvailable;
}
