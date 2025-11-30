//package com.itsutra.project.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AnalysisService {
//
//    private final AIProviderStrategy aiProviderStrategy;
//    private final AnalysisRepository analysisRepository;
//    private final ObjectMapper objectMapper;
//
//    public ResponseAnalysisResponse analyzeResponse(ResponseAnalysisRequest request) {
//        try {
//            String prompt = buildResponseAnalysisPrompt(request);
//            String aiResponse = aiProviderStrategy.generateContent(prompt);
//
//            ResponseAnalysisResponse response = parseResponseAnalysis(aiResponse);
//            saveAnalysisResult(request, response);
//
//            return response;
//
//        } catch (Exception e) {
//            log.error("Error analyzing response", e);
//            return createDefaultResponseAnalysis();
//        }
//    }
//
//    public SentimentAnalysisResponse analyzeSentiment(SentimentAnalysisRequest request) {
//        try {
//            String prompt = buildSentimentAnalysisPrompt(request);
//            String aiResponse = aiProviderStrategy.generateContent(prompt);
//
//            SentimentAnalysisResponse response = parseSentimentAnalysis(aiResponse);
//            saveSentimentAnalysis(request, response);
//
//            return response;
//
//        } catch (Exception e) {
//            log.error("Error analyzing sentiment", e);
//            return createDefaultSentimentAnalysis();
//        }
//    }
//
//    private String buildResponseAnalysisPrompt(ResponseAnalysisRequest request) {
//        return String.format("""
//            Analyze the following interview response:
//
//            Question: %s
//            Candidate Response: %s
//            Context: %s
//
//            Please evaluate based on:
//            1. Technical accuracy and knowledge
//            2. Communication clarity
//            3. Completeness of answer
//            4. Problem-solving approach
//            5. Confidence and professionalism
//
//            Provide scores (0-10) for each category and overall feedback.
//            """,
//                request.getQuestion(),
//                request.getCandidateResponse(),
//                request.getContext()
//        );
//    }
//
//    private String buildSentimentAnalysisPrompt(SentimentAnalysisRequest request) {
//        return String.format("""
//            Analyze the sentiment and emotional tone of this interview transcript:
//
//            %s
//
//            Focus areas: %s
//
//            Provide:
//            1. Overall sentiment score (-1 to 1)
//            2. Key emotional aspects
//            3. Confidence level
//            4. Engagement level
//            5. Any concerning patterns
//            """,
//                request.getTranscript(),
//                String.join(", ", request.getFocusAreas() != null ? request.getFocusAreas() : List.of())
//        );
//    }
//
//    private ResponseAnalysisResponse parseResponseAnalysis(String aiResponse) {
//        // Parse AI response - simplified for example
//        ResponseAnalysisResponse response = new ResponseAnalysisResponse();
//        response.setTechnicalScore(7.5);
//        response.setCommunicationScore(8.0);
//        response.setCompletenessScore(6.5);
//        response.setOverallScore(7.3);
//        response.setStrengths(Arrays.asList("Good technical knowledge", "Clear communication"));
//        response.setImprovementAreas(Arrays.asList("Could provide more examples", "Should structure answer better"));
//        response.setDetailedFeedback("The candidate demonstrated solid understanding but could improve with more practical examples.");
//        response.setSentiment("POSITIVE");
//
//        Map<String, Double> skillScores = new HashMap<>();
//        skillScores.put("Technical Knowledge", 7.5);
//        skillScores.put("Communication", 8.0);
//        skillScores.put("Problem Solving", 6.0);
//        response.setSkillScores(skillScores);
//
//        return response;
//    }
//
//    private SentimentAnalysisResponse parseSentimentAnalysis(String aiResponse) {
//        // Parse AI response - simplified for example
//        SentimentAnalysisResponse response = new SentimentAnalysisResponse();
//        response.setOverallSentiment(0.7);
//        response.setDominantEmotion("CONFIDENT");
//        response.setRequiresFollowUp(false);
//
//        Map<String, Double> aspectSentiments = new HashMap<>();
//        aspectSentiments.put("Confidence", 0.8);
//        aspectSentiments.put("Engagement", 0.6);
//        aspectSentiments.put("Professionalism", 0.9);
//        response.setAspectSentiments(aspectSentiments);
//
//        return response;
//    }
//
//    private void saveAnalysisResult(ResponseAnalysisRequest request, ResponseAnalysisResponse response) {
//        try {
//            Analysis analysis = Analysis.builder()
//                    .candidateId(request.getCandidateId())
//                    .interviewId(request.getInterviewId())
//                    .analysisType(Analysis.AnalysisType.RESPONSE_QUALITY)
//                    .transcriptText(request.getCandidateResponse())
//                    .analysisResults(objectMapper.writeValueAsString(response))
//                    .sentimentScore(convertToSentimentScore(response.getSentiment()))
//                    .confidenceLevel(0.85)
//                    .keyInsights("Good technical foundation with room for improvement in examples")
//                    .improvementSuggestions("Practice with more real-world scenarios")
//                    .build();
//
//            analysisRepository.save(analysis);
//
//        } catch (JsonProcessingException e) {
//            log.error("Error saving analysis result", e);
//        }
//    }
//
//    private void saveSentimentAnalysis(SentimentAnalysisRequest request, SentimentAnalysisResponse response) {
//        try {
//            Analysis analysis = Analysis.builder()
//                    .interviewId(request.getInterviewId())
//                    .analysisType(Analysis.AnalysisType.COMMUNICATION_SKILLS)
//                    .transcriptText(request.getTranscript())
//                    .analysisResults(objectMapper.writeValueAsString(response))
//                    .sentimentScore(response.getOverallSentiment())
//                    .confidenceLevel(0.90)
//                    .keyInsights("Candidate shows confidence and good communication skills")
//                    .improvementSuggestions("Maintain consistent engagement throughout")
//                    .build();
//
//            analysisRepository.save(analysis);
//
//        } catch (JsonProcessingException e) {
//            log.error("Error saving sentiment analysis", e);
//        }
//    }
//
//    private Double convertToSentimentScore(String sentiment) {
//        return switch (sentiment.toUpperCase()) {
//            case "POSITIVE" -> 0.8;
//            case "NEUTRAL" -> 0.0;
//            case "NEGATIVE" -> -0.5;
//            default -> 0.0;
//        };
//    }
//
//    private ResponseAnalysisResponse createDefaultResponseAnalysis() {
//        ResponseAnalysisResponse response = new ResponseAnalysisResponse();
//        response.setTechnicalScore(5.0);
//        response.setCommunicationScore(5.0);
//        response.setCompletenessScore(5.0);
//        response.setOverallScore(5.0);
//        response.setStrengths(List.of("Adequate response"));
//        response.setImprovement
