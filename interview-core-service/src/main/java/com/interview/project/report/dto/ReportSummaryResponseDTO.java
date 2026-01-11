package com.interview.project.report.dto;

import com.interview.project.report.enums.ReportCategory;
import lombok.Data;

@Data
public class ReportSummaryResponseDTO {
    private Long id;
    private String name;
    private String code;
    private ReportCategory category;
}
