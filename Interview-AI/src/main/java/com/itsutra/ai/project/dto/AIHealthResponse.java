package com.itsutra.ai.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIHealthResponse {

    private String serviceName;
    private String status;
    private Map<String, String> modelStatus;
    private Integer queueSize;
    private BigDecimal averageProcessingTime;
    private Integer totalRequestsToday;
    private LocalDateTime lastChecked;
}
