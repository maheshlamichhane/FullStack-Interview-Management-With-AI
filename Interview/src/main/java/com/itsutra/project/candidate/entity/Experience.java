package com.itsutra.project.candidate.entity;

import com.itsutra.project.candidate.enums.EmploymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;


@Table(name = "experiences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experience {


    @Id
    private Long id;

    private String companyName;

    private String position;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private Boolean isCurrent = false;

    private String location;

    private EmploymentType employmentType;

    private String skillsUsed;
}
