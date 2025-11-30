package com.itsutra.project.dto;

import com.itsutra.project.enums.ParticipantRole;
import lombok.Data;

@Data
public class ParticipantUpdateDTO {
    private Long id;
    private ParticipantRole role;
    private Boolean isRequired;
    private Boolean confirmedAttendance;
    private Boolean attended;
}
