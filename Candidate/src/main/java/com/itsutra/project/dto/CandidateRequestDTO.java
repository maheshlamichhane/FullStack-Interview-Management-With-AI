package com.itsutra.project.dto;

import com.itsutra.project.enums.EmploymentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CandidateRequestDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String phone;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String currentCompany;
    private String currentPosition;
    private Double totalExperience;
    private Double currentSalary;
    private Double expectedSalary;
    private Integer noticePeriod;
    private EmploymentStatus employmentStatus;
    private String preferredLocation;
    private String currentLocation;
    private Boolean willingToRelocate;
    private String source;
}
