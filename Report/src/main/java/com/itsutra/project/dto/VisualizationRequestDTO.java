package com.itsutra.project.dto;

import com.itsutra.project.enums.VisualizationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class VisualizationRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Type is required")
    private VisualizationType type;

    private Map<String, Object> config;
    private String dataQuery;
    private Integer width;
    private Integer height;
    private Boolean isInteractive;
    private Integer refreshInterval;
    private Long reportId;
    private Long dashboardId;
}
