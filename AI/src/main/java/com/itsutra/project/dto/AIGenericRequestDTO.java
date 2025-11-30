package com.itsutra.project.dto;

import com.itsutra.project.enums.RequestType;
import lombok.Data;

import java.util.Map;

@Data
public class AIGenericRequestDTO {
    private RequestType requestType;
    private Map<String, Object> parameters;
    private Long candidateId;
    private Long interviewId;
    private Long jobPositionId;
}