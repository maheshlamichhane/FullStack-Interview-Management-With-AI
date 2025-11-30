package com.itsutra.project.dto;

import com.itsutra.project.entity.Education;
import com.itsutra.project.enums.EducationLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationRequestDTO {
    @NotBlank(message = "Institution is required")
    private String institution;

    @NotBlank(message = "Degree is required")
    private String degree;

    private String fieldOfStudy;
    private String grade;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrent;
    private String description;
    private EducationLevel educationLevel;
}
