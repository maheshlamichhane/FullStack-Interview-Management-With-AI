package com.itsutra.project.candidate.entity;


import com.itsutra.project.candidate.enums.EmploymentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {


    @Id
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    private String phone;

    private String linkedinUrl;

    private String githubUrl;

    private String portfolioUrl;

    private String currentCompany;

    private String currentPosition;


    private Double totalExperience; // in years

    private Double currentSalary;

    private Double expectedSalary;

    private Integer noticePeriod; // in days

    @Builder.Default
    private Boolean isActive = true;

    private EmploymentStatus employmentStatus;

    private String preferredLocation;

    private String currentLocation;

    @Builder.Default
    private Boolean willingToRelocate = false;

    private String source;

    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

}
