package com.ai.project.controller;//package com.itsutra.ai.project.controller;
//
//
//import com.itsutra.ai.project.dto.*;
//import com.itsutra.ai.project.service.AIService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.CompletableFuture;
//
//@RestController
//@RequestMapping("/api/v1/ai")
//@RequiredArgsConstructor
//public class AIController {
//
//    private final AIService aiService;
//
//    @PostMapping("/process")
//    public ResponseEntity<AIResponseDTO> processRequest(@Valid @RequestBody AIRequestDTO request) {
//        AIResponseDTO response = aiService.processRequest(request);
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
//    }
//
//    @PostMapping("/process/async")
//    public ResponseEntity<CompletableFuture<AIResponseDTO>> processRequestAsync(
//            @Valid @RequestBody AIRequestDTO request) {
//        CompletableFuture<AIResponseDTO> future = aiService.processRequestAsync(request);
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(future);
//    }
//
//    @PostMapping("/interview/analyze")
//    public ResponseEntity<InterviewAnalysisResponse> analyzeInterview(
//            @Valid @RequestBody InterviewAnalysisRequest request) {
//        InterviewAnalysisResponse response = aiService.analyzeInterview(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/resume/analyze")
//    public ResponseEntity<ResumeAnalysisResponse> analyzeResume(
//            @Valid @RequestBody ResumeAnalysisRequest request) {
//        ResumeAnalysisResponse response = aiService.analyzeResume(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/questions/generate")
//    public ResponseEntity<QuestionGenerationResponse> generateQuestions(
//            @Valid @RequestBody QuestionGenerationRequest request) {
//        QuestionGenerationResponse response = aiService.generateQuestions(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/sentiment/analyze")
//    public ResponseEntity<SentimentAnalysisResponse> analyzeSentiment(
//            @Valid @RequestBody SentimentAnalysisRequest request) {
//        SentimentAnalysisResponse response = aiService.analyzeSentiment(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/status/{requestId}")
//    public ResponseEntity<AIResponseDTO> getRequestStatus(@PathVariable String requestId) {
//        AIResponseDTO response = aiService.getRequestStatus(requestId);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/health")
//    public ResponseEntity<AIHealthResponse> getHealth() {
//        AIHealthResponse response = aiService.getServiceHealth();
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/user/{userId}/requests")
//    public ResponseEntity<List<AIResponseDTO>> getUserRequests(@PathVariable UUID userId) {
//        List<AIResponseDTO> requests = aiService.getUserRequests(userId);
//        return ResponseEntity.ok(requests);
//    }
//
//    @DeleteMapping("/cancel/{requestId}")
//    public ResponseEntity<Void> cancelRequest(@PathVariable String requestId) {
//        aiService.cancelRequest(requestId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/batch")
//    public ResponseEntity<BatchAIResponse> processBatch(
//            @Valid @RequestBody BatchAIRequest request) {
//        BatchAIResponse response = aiService.processBatch(request);
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
//    }
//}
