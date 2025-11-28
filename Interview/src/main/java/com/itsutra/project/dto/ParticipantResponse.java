package com.itsutra.project.dto;

import com.itsutra.project.enums.ParticipantRole;
import com.itsutra.project.enums.ParticipantType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipantResponse {


    private Long id;
    private Long interviewId;
    private Long participantId;
    private ParticipantType participantType;
    private ParticipantRole role;
    private Boolean isRequired;
    private Boolean confirmedAttendance;
    private Boolean attended;
    private LocalDateTime createdAt;
}
