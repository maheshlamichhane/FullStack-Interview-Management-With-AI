package com.itsutra.project.interview.entity;


import com.itsutra.project.interview.enums.Recommendation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @NotNull
    @Column(name = "provided_by", nullable = false)
    private Long providedBy; // interviewer ID

    @NotNull
    @Column(name = "provided_for", nullable = false)
    private Long providedFor; // candidate ID

    @Min(1)
    @Max(5)
    @Column(name = "technical_skills_rating")
    private Integer technicalSkillsRating;

    @Min(1)
    @Max(5)
    @Column(name = "communication_skills_rating")
    private Integer communicationSkillsRating;

    @Min(1)
    @Max(5)
    @Column(name = "problem_solving_rating")
    private Integer problemSolvingRating;

    @Min(1)
    @Max(5)
    @Column(name = "cultural_fit_rating")
    private Integer culturalFitRating;

    @Min(1)
    @Max(5)
    @Column(name = "overall_rating")
    private Integer overallRating;

    @Column(name = "strengths", columnDefinition = "TEXT")
    private String strengths;

    @Column(name = "areas_for_improvement", columnDefinition = "TEXT")
    private String areasForImprovement;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "recommendation")
    @Enumerated(EnumType.STRING)
    private Recommendation recommendation;

    @Column(name = "is_final_feedback")
    private Boolean isFinalFeedback = false;

    @Column(name = "is_shared_with_candidate")
    private Boolean isSharedWithCandidate = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
