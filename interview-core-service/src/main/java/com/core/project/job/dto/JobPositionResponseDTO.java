package com.core.project.job.dto;

import com.core.project.job.enums.EmploymentType;
import com.core.project.job.enums.ExperienceLevel;
import com.core.project.job.enums.JobStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobPositionResponseDTO {
    private Long id;
    private String title;
    private String code;
    private String description;
    private String responsibilities;
    private String requirements;
    private String benefits;
    private DepartmentResponseDTO department;
    private LocationResponseDTO location;
    private EmploymentType employmentType;
    private ExperienceLevel experienceLevel;
    private Double minSalary;
    private Double maxSalary;
    private String salaryCurrency;
    private Integer openPositions;
    private Integer filledPositions;
    private Integer remainingPositions;
    private JobStatus status;
    private Boolean isRemote;
    private Boolean isHybrid;
    private Boolean isActive;
    private Boolean isAcceptingApplications;
    private LocalDateTime publishedAt;
    private LocalDateTime closedAt;
    private LocalDateTime applicationDeadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<JobRequirementResponseDTO> jobRequirements;
    private List<JobSkillResponseDTO> requiredSkills;
}
