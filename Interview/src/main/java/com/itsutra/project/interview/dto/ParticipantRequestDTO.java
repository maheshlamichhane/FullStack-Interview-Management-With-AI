package com.itsutra.project.interview.dto;

import com.itsutra.project.interview.enums.ParticipantRole;
import com.itsutra.project.interview.enums.ParticipantType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParticipantRequestDTO {


    @NotNull(message = "Interview ID is required")
    private Long interviewId;

    @NotNull(message = "Participant ID is required")
    private Long participantId;

    @NotNull(message = "Participant type is required")
    private ParticipantType participantType;

    @NotNull(message = "Role is required")
    private ParticipantRole role;

    private Boolean isRequired = true;
    private Boolean confirmedAttendance = false;
    private Boolean attended;
}
