package com.itsutra.project.dto;


import com.itsutra.project.enums.EmploymentStatus;
import lombok.Data;

import java.util.List;

@Data
public class CandidateSearchRequestDTO {
    private String name;
    private String email;
    private String skill;
    private List<String> skills; // Multiple skills search
    private String location;
    private Double minExperience;
    private Double maxExperience;
    private Integer maxNoticePeriod;
    private EmploymentStatus employmentStatus;
    private String currentCompany;
    private Boolean willingToRelocate;
    private Boolean onlyActive = true;

    // Advanced search fields
    private String educationLevel;
    private String degree;
    private String institution;
    private Double minSalary;
    private Double maxSalary;
    private String source;
}
