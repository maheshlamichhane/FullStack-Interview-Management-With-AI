package com.itsutra.project.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsutra.project.dto.AIResponseDTO;
import com.itsutra.project.dto.AnalysisResultDTO;
import com.itsutra.project.entity.AIResponse;
import com.itsutra.project.entity.Analysis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AIMapper {

    private final ObjectMapper objectMapper;

    public AIResponseDTO toAIResponseDto(AIResponse aiResponse) {
        AIResponseDTO dto = new AIResponseDTO();
        dto.setId(aiResponse.getId());
        dto.setSessionId(aiResponse.getRequest().getSessionId());
        dto.setRequestType(aiResponse.getRequest().getRequestType().name());
        dto.setConfidenceScore(aiResponse.getConfidenceScore());
        dto.setStatus(aiResponse.getStatus().name());
        dto.setCreatedAt(aiResponse.getCreatedAt());

        try {
            if (aiResponse.getResponseData() != null) {
                dto.setResponseData(objectMapper.readValue(aiResponse.getResponseData(), Object.class));
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing response data for AI response {}", aiResponse.getId(), e);
        }

        return dto;
    }

    public AnalysisResultDTO toAnalysisResultDto(Analysis analysis) {
        AnalysisResultDTO dto = new AnalysisResultDTO();
        dto.setId(analysis.getId());
        dto.setCandidateId(analysis.getCandidateId());
        dto.setInterviewId(analysis.getInterviewId());
        dto.setAnalysisType(analysis.getAnalysisType().name());
        dto.setSentimentScore(analysis.getSentimentScore());
        dto.setConfidenceLevel(analysis.getConfidenceLevel());
        dto.setKeyInsights(analysis.getKeyInsights());
        dto.setCreatedAt(analysis.getCreatedAt());

        try {
            if (analysis.getAnalysisResults() != null) {
                dto.setResults(objectMapper.readValue(analysis.getAnalysisResults(), Object.class));
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing analysis results for analysis {}", analysis.getId(), e);
        }

        return dto;
    }
}
