//package com.itsutra.ai.project.service;
//
//
//import com.itsutra.ai.project.dto.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.CompletableFuture;
//
//public interface AIService {
//
//    // Core AI Processing
//    AIResponseDTO processRequest(AIRequestDTO request);
//    CompletableFuture<AIResponseDTO> processRequestAsync(AIRequestDTO request);
//    BatchAIResponse processBatch(BatchAIRequest batchRequest);
//
//    // Interview Analysis
//    InterviewAnalysisResponse analyzeInterview(InterviewAnalysisRequest request);
//    CompletableFuture<InterviewAnalysisResponse> analyzeInterviewAsync(InterviewAnalysisRequest request);
//
//    // Resume Analysis
//    ResumeAnalysisResponse analyzeResume(ResumeAnalysisRequest request);
//    CompletableFuture<ResumeAnalysisResponse> analyzeResumeAsync(ResumeAnalysisRequest request);
//
//    // Question Generation
//    QuestionGenerationResponse generateQuestions(QuestionGenerationRequest request);
//    CompletableFuture<QuestionGenerationResponse> generateQuestionsAsync(QuestionGenerationRequest request);
//
//    // Sentiment Analysis
//    SentimentAnalysisResponse analyzeSentiment(SentimentAnalysisRequest request);
//
//    // Health & Monitoring
//    AIHealthResponse getServiceHealth();
//    Map<String, Object> getMetrics();
//
//    // Utility Methods
//    AIResponseDTO getRequestStatus(String requestId);
//    void cancelRequest(String requestId);
//    List<AIResponseDTO> getUserRequests(UUID userId);
//}
