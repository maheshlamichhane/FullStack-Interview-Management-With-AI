package com.itsutra.project.job.dto;


import com.itsutra.project.job.enums.EmploymentType;
import com.itsutra.project.job.enums.ExperienceLevel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobPositionUpdateRequestDTO {
    private String title;
    private String description;
    private String responsibilities;
    private String requirements;
    private String benefits;
    private Long departmentId;
    private Long locationId;
    private EmploymentType employmentType;
    private ExperienceLevel experienceLevel;
    private Double minSalary;
    private Double maxSalary;
    private String salaryCurrency;
    private Integer openPositions;
    private Boolean isRemote;
    private Boolean isHybrid;
    private LocalDateTime applicationDeadline;
}
