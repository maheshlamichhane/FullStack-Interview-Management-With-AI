package com.itsutra.project.candidate.dto;


import com.itsutra.project.candidate.enums.EmploymentType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExperienceRequestDTO {
    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Position is required")
    private String position;

    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrent;
    private String location;
    private EmploymentType employmentType;
    private List<String> skillsUsed;
}
