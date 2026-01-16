package com.core.project.report.dto;

import com.core.project.report.enums.ReportCategory;
import com.core.project.report.enums.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ReportRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Code is required")
    private String code;

    @NotNull(message = "Report type is required")
    private ReportType reportType;

    @NotNull(message = "Category is required")
    private ReportCategory category;

    private String sqlQuery;
    private String dataSource;
    private List<ReportParameterRequestDTO> parameters;
    private List<ReportColumnRequestDTO> columns;
    private Integer refreshFrequency;
    private Boolean isScheduled;
    private Boolean isPublic;
    private Integer cacheDuration;
}