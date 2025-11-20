package com.itsutra.project.entity;

import com.itsutra.project.enums.Recommendation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_feedback")
@Getter
@Setter
@NoArgsConstructor
public class InterviewFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private InterviewParticipant participant;

    private String feedbackText;
    private Integer rating;
    private String strengths;
    private String areasForImprovement;

    @Enumerated(EnumType.STRING)
    private Recommendation recommendation;

    @CreationTimestamp
    private LocalDateTime submittedAt;
}