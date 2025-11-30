package com.itsutra.project.dto;

import com.itsutra.project.enums.ReportCategory;
import lombok.Data;

@Data
public class ReportSummaryResponseDTO {
    private Long id;
    private String name;
    private String code;
    private ReportCategory category;
}
