package com.core.project.candidate.dto;

import com.core.project.candidate.enums.EmploymentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CandidateResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
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
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ResumeResponseDTO> resumes;
    private List<ExperienceResponseDTO> experiences;
    private List<EducationResponseDTO> educations;
    private List<CandidateSkillResponseDTO> skills;
}