package com.itsutra.project.report.dto;

import com.itsutra.project.report.enums.ReportCategory;
import lombok.Data;

@Data
public class ReportSummaryResponseDTO {
    private Long id;
    private String name;
    private String code;
    private ReportCategory category;
}
