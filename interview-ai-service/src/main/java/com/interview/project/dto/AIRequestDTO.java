package com.interview.project.dto;

import com.interview.project.enums.AIServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRequestDTO {

    @NotNull(message = "Service type is required")
    private AIServiceType serviceType;

    private UUID userId;
    private UUID candidateId;
    private UUID interviewId;
    private UUID jobId;

    @NotBlank(message = "Input data is required")
    private String inputData;

    private Map<String, Object> parameters;
    private String callbackUrl;
}