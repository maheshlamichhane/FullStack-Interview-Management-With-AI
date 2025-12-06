package com.itsutra.project.report.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportColumnRequestDTO {
    @NotBlank
    private String name;
    @NotBlank private String dataType;
    private String displayName;
    private Boolean sortable;
    private Boolean filterable;
    private String format;
}
