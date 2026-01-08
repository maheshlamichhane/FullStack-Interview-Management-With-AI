//package com.itsutra.project.job.entity;
//
//import com.itsutra.project.job.enums.RequirementType;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "job_requirements")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class JobRequirement {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "job_position_id", nullable = false)
//    private JobPosition jobPosition;
//
//    @Column(name = "requirement_type", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private RequirementType requirementType;
//
//    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
//    private String description;
//
//    @Column(name = "is_mandatory")
//    @Builder.Default
//    private Boolean isMandatory = true;
//
//    @Column(name = "priority")
//    @Builder.Default
//    private Integer priority = 1;
//
//}
