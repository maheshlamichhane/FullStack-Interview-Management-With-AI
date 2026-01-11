package com.interview.project.job.dto;

import com.interview.project.job.enums.EmploymentType;
import com.interview.project.job.enums.ExperienceLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobPositionRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Code is required")
    private String code;

    private String description;
    private String responsibilities;
    private String requirements;
    private String benefits;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "Location ID is required")
    private Long locationId;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @NotNull(message = "Experience level is required")
    private ExperienceLevel experienceLevel;

    private Double minSalary;
    private Double maxSalary;
    private String salaryCurrency;
    private Integer openPositions;
    private Boolean isRemote;
    private Boolean isHybrid;
    private LocalDateTime applicationDeadline;

    private List<JobRequirementRequestDTO> jobRequirements;
    private List<JobSkillRequestDTO> requiredSkills;
}
