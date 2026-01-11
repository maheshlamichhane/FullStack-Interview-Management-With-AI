//package com.itsutra.project.job.entity;
//
//import com.itsutra.project.job.enums.ProficiencyLevel;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "job_skills")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class JobSkill {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "job_position_id", nullable = false)
//    private JobPosition jobPosition;
//
//    @Column(name = "skill_name", nullable = false)
//    private String skillName;
//
//    @Column(name = "category")
//    private String category;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "proficiency_level")
//    private ProficiencyLevel proficiencyLevel;
//
//    @Column(name = "is_mandatory")
//    @Builder.Default
//    private Boolean isMandatory = true;
//
//    @Column(name = "min_experience_years")
//    private Double minExperienceYears;
//
//    @Column(name = "priority")
//    @Builder.Default
//    private Integer priority = 1;
//
//}
