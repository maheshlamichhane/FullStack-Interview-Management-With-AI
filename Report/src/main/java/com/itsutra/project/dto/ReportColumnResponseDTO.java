package com.itsutra.project.dto;

import lombok.Data;

@Data
public class ReportColumnResponseDTO{
    private String name;
    private String dataType;
    private String displayName;
    private Boolean sortable;
    private Boolean filterable;
    private String format;
}
