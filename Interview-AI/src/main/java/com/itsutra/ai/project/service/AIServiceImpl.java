package com.itsutra.ai.project.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsutra.ai.project.dao.AIRequestRepository;
import com.itsutra.ai.project.dao.GeneratedQuestionRepository;
import com.itsutra.ai.project.dao.InterviewAnalysisRepository;
import com.itsutra.ai.project.dao.ResumeAnalysisRepository;
import com.itsutra.ai.project.dto.*;
import com.itsutra.ai.project.entity.AIRequest;
import com.itsutra.ai.project.entity.GeneratedQuestion;
import com.itsutra.ai.project.entity.InterviewAnalysis;
import com.itsutra.ai.project.entity.ResumeAnalysis;
import com.itsutra.ai.project.enums.*;
import com.itsutra.ai.project.mapper.CustomAIMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final AIRequestRepository aiRequestRepository;
    private final InterviewAnalysisRepository interviewAnalysisRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final GeneratedQuestionRepository generatedQuestionRepository;
    private final CustomAIMapper customAIMapper;
    private final OpenAIClient openAIClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PromptTemplates promptTemplates;
    private final RequestIdGenerator  requestIdGenerator;
    private final ObjectMapper objectMapper;



    @Value("${ai.openai.model}")
    private String defaultModel;

    @Value("${ai.openai.temperature}")
    private Double temperature;

    @Value("${ai.openai.max-tokens}")
    private Integer maxTokens;

    @Value("${ai.services.interview-analysis.max-questions:10}")
    private Integer maxInterviewQuestions;

    private final Map<String, CompletableFuture<AIResponseDTO>> pendingRequests = new ConcurrentHashMap<>();

    // ========== ADDITIONAL HELPER METHODS ==========

    // ========== PROMPT BUILDING METHODS ==========

    private String buildInterviewAnalysisPrompt(InterviewAnalysisRequest request) {
        return promptTemplates.buildInterviewAnalysisPrompt(
                request.getInterviewTranscript(),
                request.getJobDescription(),
                request.getQuestions(),
                request.getAnswers()
        );
    }

    private String buildResumeAnalysisPrompt(String resumeText, String jobDescription, List<String> requiredSkills) {
        return promptTemplates.buildResumeAnalysisPrompt(resumeText, jobDescription, requiredSkills);
    }

    private String buildQuestionGenerationPrompt(
            String jobDescription,
            List<String> requiredSkills,
            List<QuestionDifficulty> difficulties,
            List<String> categories,
            Integer numberOfQuestions) {

        return promptTemplates.buildQuestionGenerationPrompt(
                jobDescription,
                requiredSkills,
                difficulties,
                categories,
                numberOfQuestions != null ? numberOfQuestions : maxInterviewQuestions
        );
    }

    private String buildSentimentAnalysisPrompt(String text, String context) {
        return promptTemplates.buildSentimentAnalysisPrompt(text, context);
    }

    private String buildGenericAIPrompt(AIRequestDTO request) {
        Map<String, Object> params = request.getParameters() != null ? request.getParameters() : new HashMap<>();
        String customPrompt = (String) params.get("prompt");

        if (customPrompt != null) {
            return customPrompt;
        }

        return String.format("""
            Please process the following request for service type: %s
            
            Input Data:
            %s
            
            Parameters:
            %s
            
            Please provide a structured response based on the service requirements.
            """,
                request.getServiceType(),
                request.getInputData(),
                params
        );
    }

    // ========== RESPONSE PARSING METHODS ==========

    private Map<String, Object> parseInterviewAnalysis(String aiResponse) {
        try {
            return parseJsonResponse(aiResponse);
        } catch (Exception e) {
            log.error("Failed to parse interview analysis response: {}", e.getMessage());
            return createDefaultInterviewAnalysis();
        }
    }

    private Map<String, Object> parseResumeAnalysis(String aiResponse) {
        try {
            return parseJsonResponse(aiResponse);
        } catch (Exception e) {
            log.error("Failed to parse resume analysis response: {}", e.getMessage());
            return createDefaultResumeAnalysis();
        }
    }

    private List<Map<String, Object>> parseGeneratedQuestions(String aiResponse) {
        try {
            return parseJsonArrayResponse(aiResponse);
        } catch (Exception e) {
            log.error("Failed to parse generated questions: {}", e.getMessage());
            return createDefaultQuestions();
        }
    }

    private Map<String, Object> parseSentimentAnalysis(String aiResponse) {
        try {
            return parseJsonResponse(aiResponse);
        } catch (Exception e) {
            log.error("Failed to parse sentiment analysis: {}", e.getMessage());
            return createDefaultSentimentAnalysis();
        }
    }

    private Map<String, Object> parseJsonResponse(String json) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(json, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.warn("Failed to parse JSON response, trying to extract JSON from text: {}", e.getMessage());
            return extractJsonFromText(json);
        }
    }

    private List<Map<String, Object>> parseJsonArrayResponse(String json) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(json, new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            log.warn("Failed to parse JSON array, trying alternative approach: {}", e.getMessage());
            return Collections.singletonList(extractJsonFromText(json));
        }
    }

    private Map<String, Object> extractJsonFromText(String text) {
        // Try to find JSON object or array in the text
        String jsonPattern = "\\{.*\\}|\\[.*\\]";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(jsonPattern, java.util.regex.Pattern.DOTALL);
        java.util.regex.Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            try {
                String jsonStr = matcher.group();
                return new com.fasterxml.jackson.databind.ObjectMapper()
                        .readValue(jsonStr, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
            } catch (Exception e) {
                log.debug("Failed to extract JSON from text: {}", e.getMessage());
            }
        }

        // Fallback: create a simple response map
        Map<String, Object> response = new HashMap<>();
        response.put("raw_response", text);
        response.put("parsed", false);
        return response;
    }

    // ========== ENTITY CREATION METHODS ==========

    private InterviewAnalysis createInterviewAnalysis(InterviewAnalysisRequest request, Map<String, Object> parsedResult) {
        String requestId = requestIdGenerator.generateRequestId();

        // Create metadata
        Map<String, Object> analysisMetadata = new HashMap<>();
        analysisMetadata.put("jobDescription", request.getJobDescription());
        analysisMetadata.put("requiredSkills", request.getRequiredSkills());
        analysisMetadata.put("analysisTimestamp", LocalDateTime.now().toString());
        analysisMetadata.put("modelUsed", defaultModel);

//        return customAIMapper.toInterviewAnalysisEntity(
//                request,
//                request.getInterviewId(),
//                parsedResult,
//                requestId,
//                analysisMetadata
//        );
        return null;
    }

    private ResumeAnalysis createResumeAnalysis(ResumeAnalysisRequest request, Map<String, Object> parsedResult, List<Double> embedding) {
        String requestId = requestIdGenerator.generateRequestId();

        ResumeAnalysis analysis = customAIMapper.toResumeAnalysisEntity(request, parsedResult, requestId);

        // Add embedding to metadata if needed
//        if (embedding != null && !embedding.isEmpty()) {
//            Map<String, Object> metadata = analysis.getMetadata() != null ?
//                    new HashMap<>(analysis.getMetadata()) : new HashMap<>();
//            metadata.put("embedding_dimensions", embedding.size());
//            metadata.put("has_embedding", true);
//            analysis.setMetadata(metadata);
//        }

        return analysis;
    }

    private List<GeneratedQuestion> parseGeneratedQuestions(
            String generatedQuestions,
            UUID jobId,
            List<QuestionDifficulty> difficulties,
            List<String> categories) {

        String requestId = requestIdGenerator.generateRequestId();
        List<Map<String, Object>> questionsData = parseGeneratedQuestions(generatedQuestions);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("jobId", jobId != null ? jobId.toString() : "unknown");
        metadata.put("generatedAt", LocalDateTime.now().toString());
        metadata.put("modelUsed", defaultModel);
        metadata.put("difficulties", difficulties != null ? difficulties : Arrays.asList(QuestionDifficulty.values()));
        metadata.put("categories", categories != null ? categories : Arrays.asList("Technical", "Behavioral", "Cultural"));

//        return customAIMapper.toGeneratedQuestionEntities(
//                questionsData,
//                jobId,
//                requestId,
//                defaultModel,
//                metadata
//        );
        return null;
    }

    // ========== DEFAULT RESPONSE METHODS ==========

    private Map<String, Object> createDefaultInterviewAnalysis() {
        Map<String, Object> defaultAnalysis = new HashMap<>();
        defaultAnalysis.put("overallScore", 0.0);
        defaultAnalysis.put("technicalScore", 0.0);
        defaultAnalysis.put("communicationScore", 0.0);
        defaultAnalysis.put("problemSolvingScore", 0.0);
        defaultAnalysis.put("strengths", Arrays.asList("Unable to analyze - default response"));
        defaultAnalysis.put("weaknesses", Arrays.asList("Unable to analyze - default response"));
        defaultAnalysis.put("feedbackSummary", "Analysis failed. Please try again or contact support.");
        defaultAnalysis.put("improvementSuggestions", "Analysis service unavailable.");
        defaultAnalysis.put("sentimentAnalysis", Map.of("sentiment", "NEUTRAL", "confidence", 0.0));
        defaultAnalysis.put("keywordMatches", Map.of("matchedSkills", Arrays.asList()));
        defaultAnalysis.put("isDefault", true);
        return defaultAnalysis;
    }

    private Map<String, Object> createDefaultResumeAnalysis() {
        Map<String, Object> defaultAnalysis = new HashMap<>();
        defaultAnalysis.put("skills", Arrays.asList());
        defaultAnalysis.put("experienceYears", 0);
        defaultAnalysis.put("educationLevel", "Unknown");
        defaultAnalysis.put("jobTitleMatches", Map.of());
        defaultAnalysis.put("skillGaps", Map.of("missingSkills", Arrays.asList()));
        defaultAnalysis.put("overallScore", 0.0);
        defaultAnalysis.put("summary", "Unable to analyze resume. Please ensure the text is clear and try again.");
        defaultAnalysis.put("extractedEntities", Map.of());
        defaultAnalysis.put("isDefault", true);
        return defaultAnalysis;
    }

    private List<Map<String, Object>> createDefaultQuestions() {
        Map<String, Object> defaultQuestion = new HashMap<>();
        defaultQuestion.put("questionText", "Tell me about your experience with relevant technologies.");
        defaultQuestion.put("category", "Technical");
        defaultQuestion.put("difficulty", "MEDIUM");
        defaultQuestion.put("expectedAnswer", "Candidate should discuss their experience, projects, and learnings.");
        defaultQuestion.put("evaluationCriteria", Map.of(
                "keyPoints", Arrays.asList("Relevance", "Depth", "Clarity"),
                "redFlags", Arrays.asList("Vague responses", "Lack of examples"),
                "greenFlags", Arrays.asList("Specific examples", "Clear explanations")
        ));
        defaultQuestion.put("tags", Arrays.asList("default", "fallback"));
        defaultQuestion.put("isDefault", true);

        return Arrays.asList(defaultQuestion);
    }

    private Map<String, Object> createDefaultSentimentAnalysis() {
        Map<String, Object> defaultSentiment = new HashMap<>();
        defaultSentiment.put("sentiment", "NEUTRAL");
        defaultSentiment.put("confidence", 0.0);
        defaultSentiment.put("emotionScores", Map.of(
                "positive", 0.0,
                "negative", 0.0,
                "neutral", 1.0
        ));
        defaultSentiment.put("keyPhrases", Arrays.asList("Unable to analyze sentiment"));
        defaultSentiment.put("isDefault", true);
        return defaultSentiment;
    }

    // ========== DISTRIBUTION CALCULATION METHODS ==========

    private Map<String, Integer> calculateDifficultyDistribution(List<GeneratedQuestion> questions) {
        Map<String, Integer> distribution = new HashMap<>();

        for (QuestionDifficulty difficulty : QuestionDifficulty.values()) {
            distribution.put(difficulty.name(), 0);
        }

        for (GeneratedQuestion question : questions) {
            String difficulty = question.getDifficulty().name();
            distribution.put(difficulty, distribution.getOrDefault(difficulty, 0) + 1);
        }

        return distribution;
    }

    private Map<String, Integer> calculateCategoryDistribution(List<GeneratedQuestion> questions) {
        Map<String, Integer> distribution = new HashMap<>();

        for (GeneratedQuestion question : questions) {
            String category = question.getCategory();
            if (category != null) {
                distribution.put(category, distribution.getOrDefault(category, 0) + 1);
            }
        }

        return distribution;
    }

    // ========== DTO MAPPING METHODS ==========

    private List<GeneratedQuestionDTO> mapToQuestionDTOs(List<GeneratedQuestion> questions) {
        return customAIMapper.toGeneratedQuestionDTOs(questions);
    }

    private InterviewAnalysisResponse mapToInterviewAnalysisResponse(InterviewAnalysis analysis) {
        return customAIMapper.toInterviewAnalysisResponse(analysis);
    }

    private ResumeAnalysisResponse mapToResumeAnalysisResponse(ResumeAnalysis analysis) {
        return customAIMapper.toResumeAnalysisResponse(analysis);
    }

    // ========== ERROR HANDLING METHODS ==========

    private void handleRequestFailure(String requestId, String errorMessage) {
        try {
            AIRequest request = aiRequestRepository.findByRequestId(requestId)
                    .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

            request.setStatus(RequestStatus.FAILED);
            request.setErrorMessage(errorMessage);
            request.setUpdatedAt(LocalDateTime.now());
            aiRequestRepository.save(request);

            // Send failure notification
            Map<String, Object> failureNotification = new HashMap<>();
            failureNotification.put("requestId", requestId);
            failureNotification.put("serviceType", request.getServiceType());
            failureNotification.put("status", "FAILED");
            failureNotification.put("error", errorMessage);
            failureNotification.put("timestamp", LocalDateTime.now());

            kafkaTemplate.send("ai-request-failed", objectMapper.writeValueAsString(failureNotification));

        } catch (Exception e) {
            log.error("Failed to handle request failure for {}: {}", requestId, e.getMessage());
        }
    }

    private void retryFailedRequest(String requestId) {
        try {
            AIRequest request = aiRequestRepository.findByRequestId(requestId)
                    .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

            if (request.getStatus() == RequestStatus.FAILED) {
                request.setStatus(RequestStatus.PENDING);
                request.setErrorMessage(null);
                request.setUpdatedAt(LocalDateTime.now());
                aiRequestRepository.save(request);

                log.info("Retrying failed request: {}", requestId);
            }
        } catch (Exception e) {
            log.error("Failed to retry request {}: {}", requestId, e.getMessage());
        }
    }

    // ========== REQUEST PROCESSING METHODS ==========

    private String processGenericAI(AIRequestDTO request) {
        String prompt = buildGenericAIPrompt(request);
        String result = openAIClient.callChatGPT(prompt, defaultModel);

        // Try to structure the response
        try {
            Map<String, Object> structuredResult = parseJsonResponse(result);
            return convertToJson(structuredResult);
        } catch (Exception e) {
            // Return raw response if parsing fails
            return result;
        }
    }


    private String convertToJson(Map<String, Object> map) throws JsonProcessingException {
        return objectMapper.writeValueAsString(map);
    }

    private String processQuestionGeneration(AIRequestDTO request) throws JsonProcessingException {
        Map<String, Object> params = request.getParameters();
        String jobDescription = (String) params.get("jobDescription");
        List<String> requiredSkills = (List<String>) params.get("requiredSkills");
        List<QuestionDifficulty> difficulties = parseDifficulties(params.get("difficulties"));
        List<String> categories = (List<String>) params.get("categories");
        Integer numberOfQuestions = (Integer) params.get("numberOfQuestions");
        UUID jobId = (UUID) params.get("jobId");

        QuestionGenerationRequest generationRequest = new QuestionGenerationRequest();
        generationRequest.setJobId(jobId);
        generationRequest.setJobDescription(jobDescription);
        generationRequest.setRequiredSkills(requiredSkills);
        generationRequest.setDifficulties(difficulties);
        generationRequest.setCategories(categories);
        generationRequest.setNumberOfQuestions(numberOfQuestions);

        QuestionGenerationResponse response = generateQuestions(generationRequest);
        return convertToJson(response);
    }
    private String convertToJson(QuestionGenerationResponse request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }


    private List<QuestionDifficulty> parseDifficulties(Object difficultiesObj) {
        if (difficultiesObj == null) {
            return Arrays.asList(QuestionDifficulty.EASY, QuestionDifficulty.MEDIUM, QuestionDifficulty.HARD);
        }

        if (difficultiesObj instanceof List) {
            return ((List<?>) difficultiesObj).stream()
                    .map(obj -> {
                        if (obj instanceof String) {
                            try {
                                return QuestionDifficulty.valueOf(((String) obj).toUpperCase());
                            } catch (IllegalArgumentException e) {
                                return QuestionDifficulty.MEDIUM;
                            }
                        } else if (obj instanceof QuestionDifficulty) {
                            return (QuestionDifficulty) obj;
                        }
                        return QuestionDifficulty.MEDIUM;
                    })
                    .collect(Collectors.toList());
        }

        return Arrays.asList(QuestionDifficulty.EASY, QuestionDifficulty.MEDIUM, QuestionDifficulty.HARD);
    }

    // ========== CACHE AND QUEUE MANAGEMENT ==========

    public void addToPendingRequests(String requestId, CompletableFuture<AIResponseDTO> future) {
        pendingRequests.put(requestId, future);

        // Set timeout for the request
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(300000); // 5 minutes timeout
                if (pendingRequests.containsKey(requestId)) {
                    handleRequestTimeout(requestId);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private void handleRequestTimeout(String requestId) {
        CompletableFuture<AIResponseDTO> future = pendingRequests.remove(requestId);
        if (future != null && !future.isDone()) {
            future.completeExceptionally(new RuntimeException("Request timeout after 5 minutes"));
            handleRequestFailure(requestId, "Request timeout");
        }
    }

    public CompletableFuture<AIResponseDTO> getPendingRequest(String requestId) {
        return pendingRequests.get(requestId);
    }

    public void removePendingRequest(String requestId) {
        pendingRequests.remove(requestId);
    }

    // ========== METRICS AND MONITORING ==========

    private Map<String, Object> getDetailedMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        // Request counts by service type
        Map<AIServiceType, Long> requestCounts = new HashMap<>();
        for (AIServiceType type : AIServiceType.values()) {
            Long count = aiRequestRepository.countCompletedByServiceType(type);
            requestCounts.put(type, count != null ? count : 0L);
        }
        metrics.put("requestCountsByService", requestCounts);

        // Status distribution
//        Map<RequestStatus, Long> statusCounts = new HashMap<>();
//        for (RequestStatus status : RequestStatus.values()) {
//            Long count = aiRequestRepository.countByStatus(status);
//            statusCounts.put(status, count != null ? count : 0L);
//        }
//        metrics.put("statusDistribution", statusCounts);

        // Processing time statistics
        Double avgProcessingTime = aiRequestRepository.getAverageProcessingTime();
        metrics.put("averageProcessingTimeMs", avgProcessingTime != null ? avgProcessingTime : 0);

        // Token usage
//        Integer totalTokens = aiRequestRepository.sumTokensUsed();
//        metrics.put("totalTokensUsed", totalTokens != null ? totalTokens : 0);

        // Cost analysis
//        BigDecimal totalCost = aiRequestRepository.sumTotalCost();
//        metrics.put("totalCost", totalCost != null ? totalCost : BigDecimal.ZERO);

        // Active requests
        metrics.put("pendingRequests", pendingRequests.size());
        metrics.put("activeThreads", Thread.activeCount());

        metrics.put("lastUpdated", LocalDateTime.now());

        return metrics;
    }

    // ========== BATCH PROCESSING METHODS ==========

    @Override
    public AIResponseDTO processRequest(AIRequestDTO request) {
        return null;
    }

    @Override
    public CompletableFuture<AIResponseDTO> processRequestAsync(AIRequestDTO request) {
        return null;
    }

    @Override
    @Transactional
    public BatchAIResponse processBatch(BatchAIRequest batchRequest) {
        String batchId = "BATCH_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        List<AIResponseDTO> results = new ArrayList<>();

        log.info("Processing batch request with {} items", batchRequest.getRequests().size());

        if (Boolean.TRUE.equals(batchRequest.getParallelProcessing())) {
            // Process in parallel
            List<CompletableFuture<AIResponseDTO>> futures = batchRequest.getRequests().stream()
                    .map(this::processRequestAsync)
                    .collect(Collectors.toList());

            // Wait for all to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            // Collect results
            for (CompletableFuture<AIResponseDTO> future : futures) {
                try {
                    results.add(future.get());
                } catch (Exception e) {
                    log.error("Error getting batch result: {}", e.getMessage());
                    results.add(createErrorResponse(e.getMessage()));
                }
            }
        } else {
            // Process sequentially
            for (AIRequestDTO request : batchRequest.getRequests()) {
                try {
                    results.add(processRequest(request));
                } catch (Exception e) {
                    log.error("Error processing batch item: {}", e.getMessage());
                    results.add(createErrorResponse(e.getMessage()));
                }
            }
        }

//        return customAIMapper.toBatchAIResponse(batchId, results);
        return null;
    }

    @Override
    public InterviewAnalysisResponse analyzeInterview(InterviewAnalysisRequest request) {
        return null;
    }

    private AIResponseDTO createErrorResponse(String errorMessage) {
        AIResponseDTO errorResponse = new AIResponseDTO();
        errorResponse.setStatus(RequestStatus.FAILED);
        errorResponse.setErrorMessage(errorMessage);
        errorResponse.setCreatedAt(LocalDateTime.now());
        return errorResponse;
    }

    // ========== UTILITY METHODS ==========

    @Override
    public AIResponseDTO getRequestStatus(String requestId) {
        return aiRequestRepository.findByRequestId(requestId)
                .map(customAIMapper::toAIResponseDTO)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));
    }

    @Override
    public void cancelRequest(String requestId) {
        AIRequest request = aiRequestRepository.findByRequestId(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

        if (request.getStatus() == RequestStatus.PENDING || request.getStatus() == RequestStatus.PROCESSING) {
            request.setStatus(RequestStatus.CANCELLED);
            request.setUpdatedAt(LocalDateTime.now());
            aiRequestRepository.save(request);

            // Cancel pending future if exists
            CompletableFuture<AIResponseDTO> future = pendingRequests.remove(requestId);
            if (future != null && !future.isDone()) {
                future.cancel(true);
            }

            log.info("Request {} cancelled", requestId);
        }
    }

    @Override
    public List<AIResponseDTO> getUserRequests(UUID userId) {
        List<AIRequest> requests = aiRequestRepository.findByUserId(userId);
        return customAIMapper.toAIResponseDTOs(requests);
    }

    @Override
    public Map<String, Object> getMetrics() {
        return getDetailedMetrics();
    }

    // Add this method to implement the interface
    @Override
    @Async("aiTaskExecutor")
    public CompletableFuture<InterviewAnalysisResponse> analyzeInterviewAsync(InterviewAnalysisRequest request) {
        return CompletableFuture.supplyAsync(() -> analyzeInterview(request));
    }

    @Override
    public ResumeAnalysisResponse analyzeResume(ResumeAnalysisRequest request) {
        return null;
    }

    @Override
    @Async("aiTaskExecutor")
    public CompletableFuture<ResumeAnalysisResponse> analyzeResumeAsync(ResumeAnalysisRequest request) {
        return CompletableFuture.supplyAsync(() -> analyzeResume(request));
    }

    @Override
    public QuestionGenerationResponse generateQuestions(QuestionGenerationRequest request) {
        return null;
    }

    @Override
    @Async("aiTaskExecutor")
    public CompletableFuture<QuestionGenerationResponse> generateQuestionsAsync(QuestionGenerationRequest request) {
        return CompletableFuture.supplyAsync(() -> generateQuestions(request));
    }

    @Override
    public SentimentAnalysisResponse analyzeSentiment(SentimentAnalysisRequest request) {
        return null;
    }

    @Override
    public AIHealthResponse getServiceHealth() {
        return null;
    }

}
