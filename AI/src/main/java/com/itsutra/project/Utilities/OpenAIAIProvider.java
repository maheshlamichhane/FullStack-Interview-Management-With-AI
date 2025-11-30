package com.itsutra.project.Utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class OpenAIAIProvider implements AIProvider {

    @Value("${ai.provider.openai.api-key:}")
    private String apiKey;

    @Value("${ai.provider.openai.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    @Value("${ai.provider.openai.model:gpt-4}")
    private String model;

    @Override
    public String generateQuestions(String prompt, Map<String, Object> parameters) {
        try {
            // Implementation for OpenAI API call
            String fullPrompt = buildQuestionGenerationPrompt(prompt, parameters);
            // Call OpenAI API
            // return apiClient.chatCompletions(fullPrompt);
            log.info("Generating questions using OpenAI for prompt: {}", prompt);
            return "Mock AI-generated questions based on: " + prompt;
        } catch (Exception e) {
            log.error("Error generating questions with OpenAI", e);
            throw new RuntimeException("Failed to generate questions with OpenAI", e);
        }
    }

    @Override
    public Map<String, Object> analyzeAnswer(String question, String answer, Map<String, Object> context) {
        try {
            // Implementation for answer analysis
            log.info("Analyzing answer using OpenAI. Question: {}, Answer: {}", question, answer);

            // Mock response - replace with actual OpenAI API call
            return Map.of(
                    "technical_accuracy", 0.85,
                    "completeness", 0.78,
                    "relevance", 0.92,
                    "sentiment", 0.65,
                    "suggested_improvements", "Consider providing more specific examples",
                    "confidence_score", 0.88
            );
        } catch (Exception e) {
            log.error("Error analyzing answer with OpenAI", e);
            throw new RuntimeException("Failed to analyze answer with OpenAI", e);
        }
    }

    @Override
    public Map<String, Object> analyzeSentiment(String text, Map<String, Object> parameters) {
        try {
            log.info("Analyzing sentiment for text: {}", text.substring(0, Math.min(50, text.length())));

            // Mock sentiment analysis - replace with actual API call
            return Map.of(
                    "sentiment_score", 0.75, // -1.0 to 1.0
                    "sentiment_label", "POSITIVE",
                    "confidence", 0.89,
                    "key_phrases", new String[]{"confident", "knowledgeable", "experienced"},
                    "emotional_tone", "professional"
            );
        } catch (Exception e) {
            log.error("Error analyzing sentiment with OpenAI", e);
            throw new RuntimeException("Failed to analyze sentiment with OpenAI", e);
        }
    }

    @Override
    public Map<String, Object> generateRecommendations(Map<String, Object> candidateData) {
        try {
            log.info("Generating recommendations for candidate: {}", candidateData.get("candidateId"));

            // Mock recommendations - replace with actual API call
            return Map.of(
                    "hiring_recommendation", "STRONG_HIRE",
                    "confidence_score", 0.82,
                    "key_strengths", new String[]{"Technical skills", "Problem-solving", "Communication"},
                    "improvement_areas", new String[]{"Industry knowledge", "Leadership examples"},
                    "recommended_positions", new String[]{"Senior Developer", "Tech Lead"},
                    "training_suggestions", new String[]{"Advanced system design", "Leadership training"}
            );
        } catch (Exception e) {
            log.error("Error generating recommendations with OpenAI", e);
            throw new RuntimeException("Failed to generate recommendations with OpenAI", e);
        }
    }

    @Override
    public String getProviderName() {
        return "OPENAI";
    }

    @Override
    public boolean isAvailable() {
        return apiKey != null && !apiKey.trim().isEmpty();
    }

    private String buildQuestionGenerationPrompt(String prompt, Map<String, Object> parameters) {
        StringBuilder fullPrompt = new StringBuilder();
        fullPrompt.append("Generate interview questions for the following requirements:\n");
        fullPrompt.append("Job Role: ").append(parameters.get("jobRole")).append("\n");
        fullPrompt.append("Experience Level: ").append(parameters.get("experienceLevel")).append("\n");
        fullPrompt.append("Specific Skills: ").append(parameters.get("specificSkills")).append("\n");
        fullPrompt.append("Number of Questions: ").append(parameters.get("numberOfQuestions")).append("\n");
        fullPrompt.append("Question Type: ").append(parameters.get("questionType")).append("\n");
        fullPrompt.append("Additional Context: ").append(prompt).append("\n");
        fullPrompt.append("Please generate professional, relevant interview questions.");

        return fullPrompt.toString();
    }
}
