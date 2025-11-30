package com.itsutra.project.dto;

import com.itsutra.project.enums.EducationLevel;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationResponseDTO {
    private Long id;
    private Long candidateId;
    private String institution;
    private String degree;
    private String fieldOfStudy;
    private String grade;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrent;
    private String description;
    private EducationLevel educationLevel;
}
