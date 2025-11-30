package com.itsutra.project.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "skill_matchings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillMatching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @Column(name = "job_position_id", nullable = false)
    private Long jobPositionId;

    @Column(name = "required_skills", columnDefinition = "TEXT")
    private String requiredSkills; // JSON array

    @Column(name = "candidate_skills", columnDefinition = "TEXT")
    private String candidateSkills; // JSON array

    @Column(name = "matching_skills", columnDefinition = "TEXT")
    private String matchingSkills; // JSON array

    @Column(name = "missing_skills", columnDefinition = "TEXT")
    private String missingSkills; // JSON array

    @Column(name = "match_percentage")
    private Double matchPercentage;

    @Column(name = "strengths", columnDefinition = "TEXT")
    private String strengths; // JSON array

    @Column(name = "development_areas", columnDefinition = "TEXT")
    private String developmentAreas; // JSON array

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
