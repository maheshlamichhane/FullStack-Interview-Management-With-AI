package com.interview.project.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.project.dto.*;
import com.interview.project.entity.AIRequest;
import com.interview.project.entity.GeneratedQuestion;
import com.interview.project.entity.InterviewAnalysis;
import com.interview.project.entity.ResumeAnalysis;
import com.interview.project.enums.QuestionDifficulty;
import com.interview.project.enums.RequestStatus;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomAIMapper {

    private final ObjectMapper objectMapper;

    public CustomAIMapper() {
        this.objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    // ========== AI REQUEST MAPPERS ==========

    public AIRequest toAIRequestEntity(AIRequestDTO dto) {
        AIRequest entity = new AIRequest();
        entity.setRequestId(generateRequestId());
        entity.setServiceType(dto.getServiceType());
        entity.setUserId(dto.getUserId());
        entity.setCandidateId(dto.getCandidateId());
        entity.setInterviewId(dto.getInterviewId());
        entity.setInputData(dto.getInputData());
        entity.setMetadata(convertToMap(dto.getParameters()));
        entity.setStatus(RequestStatus.PENDING);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public AIResponseDTO toAIResponseDTO(AIRequest entity) {
        if (entity == null) {
            return null;
        }

        AIResponseDTO dto = new AIResponseDTO();
        dto.setId(entity.getId());
        dto.setRequestId(entity.getRequestId());
        dto.setServiceType(entity.getServiceType());
        dto.setStatus(entity.getStatus());
        dto.setOutputData(entity.getOutputData());
        dto.setErrorMessage(entity.getErrorMessage());
        dto.setModelUsed(entity.getModelUsed());
        dto.setTokensUsed(entity.getTokensUsed());
        dto.setCost(entity.getCost());
        dto.setProcessingTimeMs(entity.getProcessingTimeMs());
        dto.setMetadata(entity.getMetadata());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    public List<AIResponseDTO> toAIResponseDTOs(List<AIRequest> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toAIResponseDTO)
                .collect(Collectors.toList());
    }

    public void updateAIRequestFromDTO(AIRequestDTO dto, AIRequest entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getUserId() != null) {
            entity.setUserId(dto.getUserId());
        }
        if (dto.getCandidateId() != null) {
            entity.setCandidateId(dto.getCandidateId());
        }
        if (dto.getInterviewId() != null) {
            entity.setInterviewId(dto.getInterviewId());
        }
        if (dto.getInputData() != null) {
            entity.setInputData(dto.getInputData());
        }
        if (dto.getParameters() != null) {
            entity.setMetadata(convertToMap(dto.getParameters()));
        }

        entity.setUpdatedAt(LocalDateTime.now());
    }

    // ========== INTERVIEW ANALYSIS MAPPERS ==========

    public InterviewAnalysis toInterviewAnalysisEntity(
            InterviewAnalysisRequest request,
            UUID interviewId,
            Map<String, Object> parsedResult,
            String requestId) {

        InterviewAnalysis entity = new InterviewAnalysis();
        entity.setInterviewId(interviewId);
        entity.setRequestId(requestId);

        // Map scores
        if (parsedResult != null) {
            entity.setOverallScore(getBigDecimalValue(parsedResult.get("overallScore")));
            entity.setTechnicalScore(getBigDecimalValue(parsedResult.get("technicalScore")));
            entity.setCommunicationScore(getBigDecimalValue(parsedResult.get("communicationScore")));
            entity.setProblemSolvingScore(getBigDecimalValue(parsedResult.get("problemSolvingScore")));

            // Map lists
            entity.setStrengths(getStringList(parsedResult.get("strengths")));
            entity.setWeaknesses(getStringList(parsedResult.get("weaknesses")));

            // Map text fields
            entity.setFeedbackSummary(getStringValue(parsedResult.get("feedbackSummary")));
            entity.setImprovementSuggestions(getStringValue(parsedResult.get("improvementSuggestions")));
            entity.setGeneratedReport(getStringValue(parsedResult.get("generatedReport")));

            // Map JSON objects
            entity.setSentimentAnalysis(getMapValue(parsedResult.get("sentimentAnalysis")));
            entity.setKeywordMatches(getMapValue(parsedResult.get("keywordMatches")));
            entity.setTranscriptAnalysis(getMapValue(parsedResult.get("transcriptAnalysis")));
        }

        // Set metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("jobDescription", request.getJobDescription());
        metadata.put("requiredSkills", request.getRequiredSkills());
        metadata.put("analysisTimestamp", LocalDateTime.now().toString());
        metadata.put("questionsAsked", request.getQuestions());
        metadata.put("answersProvided", request.getAnswers());

        entity.setAnalysisMetadata(metadata);
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public InterviewAnalysisResponse toInterviewAnalysisResponse(InterviewAnalysis entity) {
        if (entity == null) {
            return null;
        }

        InterviewAnalysisResponse response = new InterviewAnalysisResponse();
        response.setAnalysisId(entity.getId());
        response.setInterviewId(entity.getInterviewId());
        response.setOverallScore(entity.getOverallScore());
        response.setTechnicalScore(entity.getTechnicalScore());
        response.setCommunicationScore(entity.getCommunicationScore());
        response.setProblemSolvingScore(entity.getProblemSolvingScore());
        response.setStrengths(entity.getStrengths());
        response.setWeaknesses(entity.getWeaknesses());
        response.setFeedbackSummary(entity.getFeedbackSummary());
        response.setImprovementSuggestions(entity.getImprovementSuggestions());
        response.setGeneratedReport(entity.getGeneratedReport());
        response.setAnalyzedAt(entity.getCreatedAt());

        // Parse JSON fields
        try{
            response.setSentimentAnalysis(parseJsonToMap(objectMapper.writeValueAsString(entity.getSentimentAnalysis())));
            response.setKeywordMatches(parseJsonToMap(objectMapper.writeValueAsString(entity.getKeywordMatches())));
        }
        catch (Exception e){

        }


        return response;
    }


    public List<InterviewAnalysisResponse> toInterviewAnalysisResponses(List<InterviewAnalysis> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toInterviewAnalysisResponse)
                .collect(Collectors.toList());
    }

    // ========== RESUME ANALYSIS MAPPERS ==========

    public ResumeAnalysis toResumeAnalysisEntity(
            ResumeAnalysisRequest request,
            Map<String, Object> parsedResult,
            String requestId) {

        ResumeAnalysis entity = new ResumeAnalysis();
        entity.setCandidateId(request.getCandidateId());
        entity.setResumeFileId(request.getResumeFileId());
        entity.setRequestId(requestId);

        if (parsedResult != null) {
            entity.setParsedData(getMapValue(parsedResult.get("parsedData")));
            entity.setSkills(getStringList(parsedResult.get("skills")));
            entity.setExperienceYears(getIntegerValue(parsedResult.get("experienceYears")));
            entity.setEducationLevel(getStringValue(parsedResult.get("educationLevel")));
            entity.setJobTitleMatches(getMapValue(parsedResult.get("jobTitleMatches")));
            entity.setSkillGaps(getMapValue(parsedResult.get("skillGaps")));
            entity.setOverallScore(getBigDecimalValue(parsedResult.get("overallScore")));
            entity.setSummary(getStringValue(parsedResult.get("summary")));
            entity.setExtractedEntities(getMapValue(parsedResult.get("extractedEntities")));
        }

        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public ResumeAnalysisResponse toResumeAnalysisResponse(ResumeAnalysis entity) {
        if (entity == null) {
            return null;
        }

        ResumeAnalysisResponse response = new ResumeAnalysisResponse();
        response.setAnalysisId(entity.getId());
        response.setCandidateId(entity.getCandidateId());
        response.setResumeFileId(entity.getResumeFileId());
        response.setSkills(entity.getSkills());
        response.setExperienceYears(entity.getExperienceYears());
        response.setEducationLevel(entity.getEducationLevel());
        response.setOverallScore(entity.getOverallScore());
        response.setSummary(entity.getSummary());
        response.setAnalyzedAt(entity.getCreatedAt());

        try{
            Map<String,Object> obj  = transformJobTitleMatches(objectMapper.writeValueAsString(entity.getSkills()));
            response.setExtractedEntities(parseJsonToMap(objectMapper.writeValueAsString(entity.getExtractedEntities())));
        }
        catch (Exception e){

        }


        // Parse JSON fields and transform data
//        response.setSkillMatches();
//        response.setSkillGaps(parseJsonToMap());


        return response;
    }

    @SneakyThrows
    public List<ResumeAnalysisResponse> toResumeAnalysisResponses(List<ResumeAnalysis> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toResumeAnalysisResponse)
                .collect(Collectors.toList());
    }

    // ========== QUESTION GENERATION MAPPERS ==========

    public GeneratedQuestion toGeneratedQuestionEntity(
            Map<String, Object> questionData,
            UUID jobId,
            String category,
            String difficulty,
            String requestId,
            String modelUsed) {

        GeneratedQuestion entity = new GeneratedQuestion();
        entity.setJobId(jobId);
        entity.setRequestId(requestId);
        entity.setCategory(category);
        entity.setDifficulty(QuestionDifficulty.valueOf(difficulty.toUpperCase()));
        entity.setQuestionText(getStringValue(questionData.get("questionText")));
        entity.setExpectedAnswer(getStringValue(questionData.get("expectedAnswer")));
        entity.setEvaluationCriteria(getMapValue(questionData.get("evaluationCriteria")));
        entity.setTags(getStringList(questionData.get("tags")));
        entity.setModelUsed(modelUsed);

        // Set metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("generatedAt", LocalDateTime.now().toString());
        metadata.put("modelVersion", modelUsed);

        entity.setMetadata(metadata);
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public List<GeneratedQuestion> toGeneratedQuestionEntities(
            List<Map<String, Object>> questionsData,
            UUID jobId,
            String requestId,
            String modelUsed) {

        if (questionsData == null) {
            return Collections.emptyList();
        }

        return questionsData.stream()
                .map(question -> {
                    String category = getStringValue(question.get("category"));
                    String difficulty = getStringValue(question.get("difficulty"));
                    return toGeneratedQuestionEntity(question, jobId, category, difficulty, requestId, modelUsed);
                })
                .collect(Collectors.toList());
    }

    public GeneratedQuestionDTO toGeneratedQuestionDTO(GeneratedQuestion entity) {
        if (entity == null) {
            return null;
        }

        GeneratedQuestionDTO dto = new GeneratedQuestionDTO();
        dto.setQuestionId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setDifficulty(entity.getDifficulty().name());
        dto.setQuestionText(entity.getQuestionText());
        dto.setExpectedAnswer(entity.getExpectedAnswer());
        dto.setTags(entity.getTags());
        dto.setModelUsed(entity.getModelUsed());

        // Parse JSON fields
        try{
            dto.setEvaluationCriteria(parseJsonToMap(objectMapper.writeValueAsString(entity.getEvaluationCriteria())));
        }
        catch (Exception e){

        }


        return dto;
    }

    @SneakyThrows
    public List<GeneratedQuestionDTO> toGeneratedQuestionDTOs(List<GeneratedQuestion> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toGeneratedQuestionDTO)
                .collect(Collectors.toList());
    }

    public QuestionGenerationResponse toQuestionGenerationResponse(
            List<GeneratedQuestion> questions,
            UUID generationId,
            String requestId) {

        QuestionGenerationResponse response = new QuestionGenerationResponse();
        response.setGenerationId(generationId);
        response.setQuestions(toGeneratedQuestionDTOs(questions));
        response.setTotalQuestions(questions.size());
        response.setGeneratedAt(LocalDateTime.now());

        // Calculate distributions
        response.setDifficultyDistribution(calculateDifficultyDistribution(questions));
        response.setCategoryDistribution(calculateCategoryDistribution(questions));

        return response;
    }

    // ========== BATCH PROCESSING MAPPERS ==========

    public BatchAIResponse toBatchAIResponse(
            String batchId,
            List<AIRequest> requests,
            List<AIResponseDTO> results) {

        BatchAIResponse response = new BatchAIResponse();
        response.setBatchId(batchId);
        response.setTotalRequests(requests.size());

        long successfulCount = results.stream()
                .filter(r -> r.getStatus() == RequestStatus.COMPLETED)
                .count();
        response.setSuccessfulRequests((int) successfulCount);
        response.setFailedRequests(results.size() - (int) successfulCount);
        response.setResults(results);
        response.setCompletedAt(LocalDateTime.now());

        return response;
    }

    // ========== HEALTH & METRICS MAPPERS ==========

    public AIHealthResponse toAIHealthResponse(
            String serviceName,
            Map<String, String> modelStatus,
            Integer queueSize,
            BigDecimal avgProcessingTime,
            Integer totalRequestsToday) {

        AIHealthResponse response = new AIHealthResponse();
        response.setServiceName(serviceName);
        response.setStatus(determineServiceStatus(modelStatus));
        response.setModelStatus(modelStatus);
        response.setQueueSize(queueSize);
        response.setAverageProcessingTime(avgProcessingTime);
        response.setTotalRequestsToday(totalRequestsToday);
        response.setLastChecked(LocalDateTime.now());

        return response;
    }

    // ========== SENTIMENT ANALYSIS MAPPERS ==========

    public SentimentAnalysisResponse toSentimentAnalysisResponse(
            String text,
            Map<String, Object> sentimentData) {

        SentimentAnalysisResponse response = new SentimentAnalysisResponse();
        response.setText(text);
        response.setSentiment(getStringValue(sentimentData.get("sentiment")));
        response.setConfidence(getBigDecimalValue(sentimentData.get("confidence")));
        response.setEmotionScores(extractEmotionScores(sentimentData));
        response.setKeyPhrases(getStringList(sentimentData.get("keyPhrases")));
        response.setDetailedAnalysis(sentimentData);

        return response;
    }

    // ========== HELPER METHODS ==========

    private String generateRequestId() {
        return "REQ_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Map<String, Object> convertToMap(Object object) {
        if (object == null) {
            return new HashMap<>();
        }

        if (object instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) object;
            return new HashMap<>(map);
        }

        try {
//            return objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
            return null;
        } catch (IllegalArgumentException e) {
            log.warn("Failed to convert object to map: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    private Map<String, Object> parseJsonToMap(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new HashMap<>();
        }

        try {
//            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            return null;
        } catch (RuntimeException e) {
            log.error("Failed to parse JSON: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    private String getStringValue(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    private BigDecimal getBigDecimalValue(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            if (obj instanceof BigDecimal) {
                return (BigDecimal) obj;
            } else if (obj instanceof Number) {
                return BigDecimal.valueOf(((Number) obj).doubleValue());
            } else if (obj instanceof String) {
                return new BigDecimal((String) obj);
            }
        } catch (Exception e) {
            log.warn("Failed to convert to BigDecimal: {}", obj);
        }

        return null;
    }

    private Integer getIntegerValue(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            if (obj instanceof Integer) {
                return (Integer) obj;
            } else if (obj instanceof Number) {
                return ((Number) obj).intValue();
            } else if (obj instanceof String) {
                return Integer.parseInt((String) obj);
            }
        } catch (Exception e) {
            log.warn("Failed to convert to Integer: {}", obj);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private List<String> getStringList(Object obj) {
        if (obj == null) {
            return new ArrayList<>();
        }

        if (obj instanceof List) {
            return ((List<?>) obj).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    private Map<String, Object> getMapValue(Object obj) {
        if (obj == null) {
            return new HashMap<>();
        }

        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            return new HashMap<>(map);
        }

        if (obj instanceof String) {
            return parseJsonToMap((String) obj);
        }

        return new HashMap<>();
    }

    private Map<String, BigDecimal> extractEmotionScores(Map<String, Object> sentimentData) {
        Map<String, BigDecimal> emotionScores = new HashMap<>();

        Object emotions = sentimentData.get("emotionScores");
        if (emotions instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> emotionMap = (Map<String, Object>) emotions;

            emotionMap.forEach((key, value) -> {
                BigDecimal score = getBigDecimalValue(value);
                if (score != null) {
                    emotionScores.put(key, score);
                }
            });
        }

        return emotionScores;
    }

    private Map<String, Object> transformJobTitleMatches(String json) {
        Map<String, Object> matches = parseJsonToMap(json);

        // Transform the structure if needed
        if (matches.containsKey("matches")) {
            Object matchesObj = matches.get("matches");
            if (matchesObj instanceof Map) {
                return (Map<String, Object>) matchesObj;
            }
        }

        return matches;
    }

    private Map<String, Integer> calculateDifficultyDistribution(List<GeneratedQuestion> questions) {
        Map<String, Integer> distribution = new HashMap<>();

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
            distribution.put(category, distribution.getOrDefault(category, 0) + 1);
        }

        return distribution;
    }

    private String determineServiceStatus(Map<String, String> modelStatus) {
        if (modelStatus == null || modelStatus.isEmpty()) {
            return "UNKNOWN";
        }

        boolean allUp = modelStatus.values().stream()
                .allMatch(status -> "UP".equalsIgnoreCase(status));

        return allUp ? "UP" : "DEGRADED";
    }
}
