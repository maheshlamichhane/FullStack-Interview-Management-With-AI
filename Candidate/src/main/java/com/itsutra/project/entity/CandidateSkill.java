package com.itsutra.project.entity;

import com.itsutra.project.enums.ProficiencyLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level")
    private ProficiencyLevel proficiencyLevel;

    @Column(name = "years_of_experience")
    private Double yearsOfExperience;

    @Column(name = "last_used")
    private Integer lastUsed; // Year when skill was last used

    @Column(name = "is_certified")
    @Builder.Default
    private Boolean isCertified = false;

    @Column(name = "certification_name")
    private String certificationName;

}
