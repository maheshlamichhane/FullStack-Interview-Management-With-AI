package com.itsutra.project.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ReportExecutionRequestDTO {
    private Map<String, Object> parameters;
    private String format;
    private Boolean async;
}
