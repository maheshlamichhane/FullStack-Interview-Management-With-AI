package com.itsutra.project.report.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ReportDataResponseDTO {
    private List<Map<String, Object>> data;
    private List<ReportColumnResponseDTO> columns;
    private Long totalRecords;
    private Map<String, Object> summary;
    private LocalDateTime generatedAt;
}
