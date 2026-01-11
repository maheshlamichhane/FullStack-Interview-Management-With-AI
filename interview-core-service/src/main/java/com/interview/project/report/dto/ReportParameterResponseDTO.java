package com.interview.project.report.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReportParameterResponseDTO {
    private String name;
    private String type;
    private String defaultValue;
    private Boolean required;
    private List<String> options;
}
