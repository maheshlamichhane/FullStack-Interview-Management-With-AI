package com.itsutra.project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AIResponseDTO {
    private Long id;
    private String sessionId;
    private String requestType;
    private Object responseData;
    private Double confidenceScore;
    private String status;
    private LocalDateTime createdAt;
}
