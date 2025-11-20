package com.itsutra.project.entity;

import com.itsutra.project.enums.ParticipantRole;
import com.itsutra.project.enums.ParticipantStatus;
import com.itsutra.project.enums.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_participants")
@Getter
@Setter
@NoArgsConstructor
public class InterviewParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private ParticipantRole participantRole;

    @Enumerated(EnumType.STRING)
    private ParticipantStatus status;

    private LocalDateTime joinedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;


}
