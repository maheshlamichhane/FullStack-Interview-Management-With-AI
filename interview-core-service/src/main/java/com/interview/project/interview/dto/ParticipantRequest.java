package com.interview.project.interview.dto;

import com.interview.project.interview.enums.ParticipantRole;
import com.interview.project.interview.enums.ParticipantType;
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
