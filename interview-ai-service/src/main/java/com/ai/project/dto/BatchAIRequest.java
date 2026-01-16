package com.ai.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchAIRequest {

    @NotNull(message = "Requests list cannot be null")
    private List<AIRequestDTO> requests;

    private Boolean parallelProcessing = true;
    private String batchId;
}
