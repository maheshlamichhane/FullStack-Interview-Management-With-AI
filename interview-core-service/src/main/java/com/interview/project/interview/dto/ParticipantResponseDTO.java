package com.interview.project.interview.dto;

import com.interview.project.interview.enums.ParticipantRole;
import com.interview.project.interview.enums.ParticipantType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipantResponseDTO {
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
