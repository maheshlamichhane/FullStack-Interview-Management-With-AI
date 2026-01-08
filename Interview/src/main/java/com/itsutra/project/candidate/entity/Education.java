package com.itsutra.project.candidate.entity;


import com.itsutra.project.candidate.enums.EducationLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;


@Table(name = "educations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {


    @Id
    private Long id;

    private Candidate candidate;

    private String institution;

    private String degree;

    private String fieldOfStudy;

    private String grade;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private Boolean isCurrent = false;

    private String description;

    private EducationLevel educationLevel;

}
