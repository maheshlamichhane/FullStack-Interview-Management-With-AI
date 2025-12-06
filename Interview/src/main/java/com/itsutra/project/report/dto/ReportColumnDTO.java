package com.itsutra.project.report.dto;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportColumnDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String dataType; // STRING, NUMBER, DATE, BOOLEAN

    private String displayName;
    private Boolean sortable;
    private Boolean filterable;
    private String format; // currency, percentage, date format
}
