package com.core.project.report.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ReportParameterRequestDTO {


    @NotBlank
    private String name;

    @NotBlank
    private String type;

    private String defaultValue;
    private Boolean required;
    private List<String> options;
}
