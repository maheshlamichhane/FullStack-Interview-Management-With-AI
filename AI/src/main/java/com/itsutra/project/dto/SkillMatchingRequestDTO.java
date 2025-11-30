package com.itsutra.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SkillMatchingRequestDTO {

    @NotNull
    private Long candidateId;

    @NotNull
    private Long jobPositionId;

    private List<String> candidateSkills;
    private List<String> jobRequiredSkills;
}
