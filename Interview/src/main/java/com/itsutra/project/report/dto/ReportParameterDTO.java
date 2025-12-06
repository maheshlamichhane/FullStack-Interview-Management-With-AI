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
public class ReportParameterDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String type; // STRING, NUMBER, DATE, BOOLEAN

    private String defaultValue;
    private Boolean required;
    private String options; // JSON array for dropdowns
}
