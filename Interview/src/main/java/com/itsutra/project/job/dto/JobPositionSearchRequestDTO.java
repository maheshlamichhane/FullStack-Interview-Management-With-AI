package com.itsutra.project.job.dto;


import com.itsutra.project.job.enums.EmploymentType;
import com.itsutra.project.job.enums.ExperienceLevel;
import lombok.Data;

import java.util.List;

@Data
public class JobPositionSearchRequestDTO {
    private String title;
    private String code;
    private Long departmentId;
    private Long locationId;
    private EmploymentType employmentType;
    private ExperienceLevel experienceLevel;
    private Double minSalary;
    private Double maxSalary;
    private Boolean isRemote;
    private Boolean isActive;
    private List<String> skills;
    private String status;
}
