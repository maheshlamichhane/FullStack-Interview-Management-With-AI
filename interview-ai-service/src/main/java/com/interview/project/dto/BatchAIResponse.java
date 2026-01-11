package com.interview.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchAIResponse {

    private String batchId;
    private Integer totalRequests;
    private Integer successfulRequests;
    private Integer failedRequests;
    private List<AIResponseDTO> results;
    private LocalDateTime completedAt;
}
