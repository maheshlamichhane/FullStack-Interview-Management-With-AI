package com.core.project.report.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class VisualizationDataResponseDTO {
    private List<Map<String, Object>> data;
    private Map<String, Object> metadata;
    private LocalDateTime generatedAt;
}
