package com.core.project.interview.entity;


import com.core.project.interview.enums.ParticipantRole;
import com.core.project.interview.enums.ParticipantType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Table(name = "interview_participants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewParticipant {


    @Id
    private Long id;

    @NotNull
    private Interview interview;

    @NotNull
    private Long participantId;


    private Long  interviewerId;

    @NotNull
    private ParticipantType participantType;

    @NotNull
    private ParticipantRole role;

    private Boolean isRequired = true;

    private Boolean confirmedAttendance = false;

    private Boolean attended;

    private LocalDateTime createdAt;
}
