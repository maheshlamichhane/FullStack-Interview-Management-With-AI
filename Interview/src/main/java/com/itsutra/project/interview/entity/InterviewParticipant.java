package com.itsutra.project.interview.entity;


import com.itsutra.project.interview.enums.ParticipantRole;
import com.itsutra.project.interview.enums.ParticipantType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_participants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @NotNull
    @Column(name = "participant_id", nullable = false)
    private Long participantId;


    @Column(name= "interviewer_id",nullable = false)
    private Long  interviewerId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "participant_type", nullable = false)
    private ParticipantType participantType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ParticipantRole role;

    @Column(name = "is_required")
    private Boolean isRequired = true;

    @Column(name = "confirmed_attendance")
    private Boolean confirmedAttendance = false;

    @Column(name = "attended")
    private Boolean attended;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
