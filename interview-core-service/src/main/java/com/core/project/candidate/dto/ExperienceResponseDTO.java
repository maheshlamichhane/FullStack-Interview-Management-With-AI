package com.core.project.candidate.dto;

import com.core.project.candidate.enums.EmploymentType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExperienceResponseDTO {
    private Long id;
    private Long candidateId;
    private String companyName;
    private String position;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrent;
    private String location;
    private EmploymentType employmentType;
    private List<String> skillsUsed;
}
