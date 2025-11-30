//package com.itsutra.project.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class QuestionService {
//
//    private final AIProviderStrategy aiProviderStrategy;
//
//    public QuestionGenerationResponse generateQuestions(QuestionGenerationRequest request) {
//        try {
//            // Generate questions using AI provider
//            String prompt = buildQuestionGenerationPrompt(request);
//            String aiResponse = aiProviderStrategy.generateContent(prompt);
//
//            // Parse AI response and structure it
//            return parseQuestionResponse(aiResponse, request);
//
//        } catch (Exception e) {
//            log.error("Error in question generation", e);
//            // Fallback to predefined questions
//            return generateFallbackQuestions(request);
//        }
//    }
//
//    private String buildQuestionGenerationPrompt(QuestionGenerationRequest request) {
//        return String.format("""
//            Generate %d interview questions for a %s %s position.
//            Technical Skills: %s
//            Soft Skills: %s
//            Difficulty Level: %s
//            Question Type: %s
//
//            Please provide questions in the following format:
//            - Technical questions testing specific skills
//            - Behavioral questions assessing soft skills
//            - Situational questions for problem-solving
//            - Include sample answers and key evaluation points
//            """,
//                request.getNumberOfQuestions(),
//                request.getExperienceLevel(),
//                request.getJobRole(),
//                String.join(", ", request.getTechnicalSkills()),
//                String.join(", ", request.getSoftSkills()),
//                request.getDifficultyLevel(),
//                request.getQuestionType()
//        );
//    }
//
//    private QuestionGenerationResponse parseQuestionResponse(String aiResponse, QuestionGenerationRequest request) {
//        // Parse AI response and convert to structured format
//        // This is a simplified version - in reality, you'd use more sophisticated parsing
//
//        QuestionGenerationResponse response = new QuestionGenerationResponse();
//        response.setSessionId(UUID.randomUUID().toString());
//        response.setDifficultyLevel(request.getDifficultyLevel());
//        response.setTotalQuestions(request.getNumberOfQuestions());
//
//        // Mock parsing - replace with actual AI response parsing
//        List<QuestionGenerationResponse.InterviewQuestion> questions = Arrays.asList(
//                createQuestion("Explain the concept of microservices architecture and its benefits.",
//                        "TECHNICAL", "System Design", List.of("Scalability", "Maintainability", "Technology Diversity")),
//                createQuestion("Describe a time when you had to handle a difficult team member.",
//                        "BEHAVIORAL", "Teamwork", List.of("Conflict Resolution", "Communication", "Leadership")),
//                createQuestion("How would you handle a situation where requirements change mid-sprint?",
//                        "SITUATIONAL", "Agile Methodology", List.of("Adaptability", "Communication", "Planning"))
//        );
//
//        response.setQuestions(questions);
//        return response;
//    }
//
//    private QuestionGenerationResponse.InterviewQuestion createQuestion(String question, String type,
//                                                                        String category, List<String> keyPoints) {
//        QuestionGenerationResponse.InterviewQuestion q = new QuestionGenerationResponse.InterviewQuestion();
//        q.setQuestion(question);
//        q.setType(type);
//        q.setCategory(category);
//        q.setKeyPoints(keyPoints);
//        q.setDifficulty("MEDIUM");
//        q.setSampleAnswer("This would be a sample answer demonstrating key points...");
//        q.setExpectedTime(5);
//        return q;
//    }
//
//    private QuestionGenerationResponse generateFallbackQuestions(QuestionGenerationRequest request) {
//        // Fallback to predefined questions based on job role
//        QuestionGenerationResponse response = new QuestionGenerationResponse();
//        response.setSessionId(UUID.randomUUID().toString());
//        response.setDifficultyLevel(request.getDifficultyLevel());
//        response.setTotalQuestions(5);
//
//        // Add some generic questions
//        List<QuestionGenerationResponse.InterviewQuestion> questions = Arrays.asList(
//                createQuestion("What are your strengths and weaknesses?", "BEHAVIORAL", "Self Assessment",
//                        List.of("Self-awareness", "Honesty", "Improvement mindset")),
//                createQuestion("Why do you want to work for our company?", "BEHAVIORAL", "Motivation",
//                        List.of("Company research", "Career goals", "Cultural fit")),
//                createQuestion("Where do you see yourself in 5 years?", "BEHAVIORAL", "Career Planning",
//                        List.of("Ambition", "Planning", "Realism"))
//        );
//
//        response.setQuestions(questions);
//        return response;
//    }
//}
