package com.itsutra.project.dto;

import com.itsutra.project.entity.InterviewParticipant;
import com.itsutra.project.enums.ParticipantRole;
import com.itsutra.project.enums.ParticipantType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParticipantRequest {


    @NotNull
    private Long participantId;

    @NotNull
    private ParticipantType participantType;

    @NotNull
    private ParticipantRole role;

    private Boolean isRequired;
}
