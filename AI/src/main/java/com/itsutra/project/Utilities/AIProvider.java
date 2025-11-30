package com.itsutra.project.Utilities;


import java.util.Map;

public interface AIProvider {
    String generateQuestions(String prompt, Map<String, Object> parameters);
    Map<String, Object> analyzeAnswer(String question, String answer, Map<String, Object> context);
    Map<String, Object> analyzeSentiment(String text, Map<String, Object> parameters);
    Map<String, Object> generateRecommendations(Map<String, Object> candidateData);
    String getProviderName();
    boolean isAvailable();
}