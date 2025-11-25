package com.itsutra.project.entity;

import com.itsutra.project.enums.InterviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
public class Interview {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    private LocalDateTime scheduledTime;
    private Integer durationMinutes;
    private String meetingUrl;

    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL)
    private List<InterviewParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL)
    private List<InterviewFeedback> feedbacks = new ArrayList<>();

    // Helper method to add participant
    public void addParticipant(InterviewParticipant participant) {
        participants.add(participant);
        participant.setInterview(this);
    }

    // Helper method to add feedback
    public void addFeedback(InterviewFeedback feedback) {
        feedbacks.add(feedback);
        feedback.setInterview(this);
    }

}
