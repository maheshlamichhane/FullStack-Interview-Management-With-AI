package com.itsutra.ai.project.dto;

import com.itsutra.ai.project.enums.AIServiceType;
import com.itsutra.ai.project.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIResponseDTO {

    private UUID id;
    private String requestId;
    private AIServiceType serviceType;
    private RequestStatus status;
    private String outputData;
    private String errorMessage;
    private String modelUsed;
    private Integer tokensUsed;
    private BigDecimal cost;
    private Long processingTimeMs;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
