package com.itsutra.project.entity;

import com.itsutra.project.enums.InterviewStatus;
import com.itsutra.project.enums.InterviewType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @NotNull
    @Column(name = "interviewer_id", nullable = false)
    private Long interviewerId;

    @Column(name = "job_position_id")
    private Long jobPositionId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "interview_type", nullable = false)
    private InterviewType interviewType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InterviewStatus status;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "scheduled_start_time", nullable = false)
    private LocalDateTime scheduledStartTime;

    @NotNull
    @Column(name = "scheduled_end_time", nullable = false)
    private LocalDateTime scheduledEndTime;

    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "meeting_link")
    private String meetingLink;

    @Column(name = "location")
    private String location;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "overall_rating")
    private Double overallRating;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InterviewSlot> slots = new ArrayList<>();

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InterviewParticipant> participants = new ArrayList<>();
}