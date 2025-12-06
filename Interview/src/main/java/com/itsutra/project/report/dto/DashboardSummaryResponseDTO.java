package com.itsutra.project.report.dto;


import com.itsutra.project.report.enums.DashboardCategory;
import lombok.Data;

@Data
public class DashboardSummaryResponseDTO {
    private Long id;
    private String name;
    private String code;
    private DashboardCategory category;
}
