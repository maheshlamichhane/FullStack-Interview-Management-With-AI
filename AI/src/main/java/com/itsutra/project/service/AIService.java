//package com.itsutra.project.service;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.itsutra.project.dao.AIResponseRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AIService {
//
//    private final AIRequestRepository aiRequestRepository;
//    private final AIResponseRepository aiResponseRepository;
//    private final AIProviderStrategy aiProviderStrategy;
//    private final ObjectMapper objectMapper;
//    private final QuestionService questionService;
//    private final AnalysisService analysisService;
//    private final RecommendationService recommendationService;
//
//    @Transactional
//    public QuestionGenerationResponse generateInterviewQuestions(QuestionGenerationRequest request) {
//        String sessionId = UUID.randomUUID().toString();
//        long startTime = System.currentTimeMillis();
//
//        try {
//            AIRequest aiRequest = createAIRequest(sessionId, AIRequest.RequestType.QUESTION_GENERATION,
//                    request, null, null, null);
//
//            QuestionGenerationResponse response = questionService.generateQuestions(request);
//
//            createAIResponse(aiRequest, response, System.currentTimeMillis() - startTime);
//
//            return response;
//
//        } catch (Exception e) {
//            log.error("Error generating interview questions", e);
//            throw new RuntimeException("Failed to generate questions: " + e.getMessage());
//        }
//    }
//
//    @Transactional
//    public ResponseAnalysisResponse analyzeCandidateResponse(ResponseAnalysisRequest request) {
//        String sessionId = UUID.randomUUID().toString();
//        long startTime = System.currentTimeMillis();
//
//        try {
//            AIRequest aiRequest = createAIRequest(sessionId, AIRequest.RequestType.RESPONSE_ANALYSIS,
//                    request, request.getCandidateId(), request.getInterviewId(), null);
//
//            ResponseAnalysisResponse response = analysisService.analyzeResponse(request);
//
//            createAIResponse(aiRequest, response, System.currentTimeMillis() - startTime);
//
//            return response;
//
//        } catch (Exception e) {
//            log.error("Error analyzing candidate response", e);
//            throw new RuntimeException("Failed to analyze response: " + e.getMessage());
//        }
//    }
//
//    @Transactional
//    public SkillMatchingResponse analyzeSkillMatching(SkillMatchingRequest request) {
//        String sessionId = UUID.randomUUID().toString();
//        long startTime = System.currentTimeMillis();
//
//        try {
//            AIRequest aiRequest = createAIRequest(sessionId, AIRequest.RequestType.SKILL_MATCHING,
//                    request, request.getCandidateId(), null, request.getJobPositionId());
//
//            SkillMatchingResponse response = recommendationService.analyzeSkillMatch(request);
//
//            createAIResponse(aiRequest, response, System.currentTimeMillis() - startTime);
//
//            return response;
//
//        } catch (Exception e) {
//            log.error("Error analyzing skill matching", e);
//            throw new RuntimeException("Failed to analyze skill matching: " + e.getMessage());
//        }
//    }
//
//    @Transactional
//    public SentimentAnalysisResponse analyzeInterviewSentiment(SentimentAnalysisRequest request) {
//        String sessionId = UUID.randomUUID().toString();
//        long startTime = System.currentTimeMillis();
//
//        try {
//            AIRequest aiRequest = createAIRequest(sessionId, AIRequest.RequestType.SENTIMENT_ANALYSIS,
//                    request, null, request.getInterviewId(), null);
//
//            SentimentAnalysisResponse response = analysisService.analyzeSentiment(request);
//
//            createAIResponse(aiRequest, response, System.currentTimeMillis() - startTime);
//
//            return response;
//
//        } catch (Exception e) {
//            log.error("Error analyzing sentiment", e);
//            throw new RuntimeException("Failed to analyze sentiment: " + e.getMessage());
//        }
//    }
//
//    private AIRequest createAIRequest(String sessionId, AIRequest.RequestType requestType,
//                                      Object requestData, Long candidateId, Long interviewId, Long jobPositionId) {
//        try {
//            AIRequest aiRequest = AIRequest.builder()
//                    .sessionId(sessionId)
//                    .requestType(requestType)
//                    .inputData(objectMapper.writeValueAsString(requestData))
//                    .candidateId(candidateId)
//                    .interviewId(interviewId)
//                    .jobPositionId(jobPositionId)
//                    .modelUsed("GPT-4")
//                    .build();
//
//            return aiRequestRepository.save(aiRequest);
//
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Error serializing request data", e);
//        }
//    }
//
//    private void createAIResponse(AIRequest aiRequest, Object responseData, long responseTime) {
//        try {
//            AIResponse aiResponse = AIResponse.builder()
//                    .request(aiRequest)
//                    .responseData(objectMapper.writeValueAsString(responseData))
//                    .confidenceScore(0.85) // Default confidence, can be calculated
//                    .status(AIResponse.ResponseStatus.SUCCESS)
//                    .build();
//
//            aiRequest.setResponseTimeMs(responseTime);
//            aiRequest.setTokensUsed(estimateTokens(responseData));
//
//            aiResponseRepository.save(aiResponse);
//
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Error serializing response data", e);
//        }
//    }
//
//    private int estimateTokens(Object data) {
//        // Simple token estimation (4 characters ≈ 1 token)
//        try {
//            String jsonString = objectMapper.writeValueAsString(data);
//            return jsonString.length() / 4;
//        } catch (JsonProcessingException e) {
//            return 100; // Default estimate
//        }
//    }
//}
