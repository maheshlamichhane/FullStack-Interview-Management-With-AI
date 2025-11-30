package com.itsutra.project.dto;

import com.itsutra.project.enums.DashboardCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DashboardRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Code is required")
    private String code;

    @NotNull(message = "Category is required")
    private DashboardCategory category;

    private Map<String, Object> layoutConfig;
    private Boolean isPublic;
    private Integer refreshInterval;
    private List<DashboardWidgetRequestDTO> widgets;
}
