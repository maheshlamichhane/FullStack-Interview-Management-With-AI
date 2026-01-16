package com.core.project.report.dto;

import com.core.project.report.enums.ReportCategory;
import lombok.Data;

@Data
public class ReportSummaryResponseDTO {
    private Long id;
    private String name;
    private String code;
    private ReportCategory category;
}
