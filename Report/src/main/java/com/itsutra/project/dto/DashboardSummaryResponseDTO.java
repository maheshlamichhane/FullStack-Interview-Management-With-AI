package com.itsutra.project.dto;

import com.itsutra.project.enums.DashboardCategory;
import lombok.Data;

@Data
public class DashboardSummaryResponseDTO {
    private Long id;
    private String name;
    private String code;
    private DashboardCategory category;
}
