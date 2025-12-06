package com.itsutra.project.interview.dto;

import com.itsutra.project.interview.enums.ParticipantRole;
import lombok.Data;

@Data
public class ParticipantUpdateDTO {
    private Long id;
    private ParticipantRole role;
    private Boolean isRequired;
    private Boolean confirmedAttendance;
    private Boolean attended;
}
