package com.itsutra.project.candidate.entity;

import com.itsutra.project.candidate.enums.ProficiencyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table(name = "candidate_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSkill {


    @Id
    private Long id;

    private String skillName;


    private ProficiencyLevel proficiencyLevel;


    private Double yearsOfExperience;

    private Integer lastUsed; // Year when skill was last used


    @Builder.Default
    private Boolean isCertified = false;

    private String certificationName;

}
