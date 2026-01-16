package com.ai.project.service;//package com.itsutra.ai.project.service;
//
//
//import com.itsutra.ai.project.enums.QuestionDifficulty;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class PromptTemplates {
//
//    public String buildInterviewAnalysisPrompt(
//            String transcript,
//            String jobDescription,
//            List<String> questions,
//            List<String> answers) {
//
//        StringBuilder prompt = new StringBuilder();
//        prompt.append("Analyze the following interview transcript and provide detailed evaluation:\n\n");
//
//        if (jobDescription != null && !jobDescription.trim().isEmpty()) {
//            prompt.append("JOB DESCRIPTION:\n").append(jobDescription).append("\n\n");
//        }
//
//        prompt.append("INTERVIEW TRANSCRIPT:\n").append(transcript).append("\n\n");
//
//        if (questions != null && !questions.isEmpty()) {
//            prompt.append("QUESTIONS ASKED:\n");
//            for (int i = 0; i < questions.size(); i++) {
//                prompt.append(i + 1).append(". ").append(questions.get(i)).append("\n");
//            }
//            prompt.append("\n");
//        }
//
//        if (answers != null && !answers.isEmpty()) {
//            prompt.append("CANDIDATE ANSWERS:\n");
//            for (int i = 0; i < answers.size(); i++) {
//                prompt.append(i + 1).append(". ").append(answers.get(i)).append("\n");
//            }
//            prompt.append("\n");
//        }
//
//        prompt.append("""
//            Please provide analysis in the following JSON format:
//            {
//                "overallScore": 85.5,
//                "technicalScore": 90.0,
//                "communicationScore": 80.0,
//                "problemSolvingScore": 85.0,
//                "strengths": ["Good technical knowledge", "Clear communication"],
//                "weaknesses": ["Could provide more detailed examples", "Rushed through some answers"],
//                "feedbackSummary": "Overall good performance with strong technical skills...",
//                "improvementSuggestions": "Practice providing more structured answers...",
//                "sentimentAnalysis": {
//                    "overallSentiment": "positive",
//                    "confidence": 0.85,
//                    "keyEmotions": ["confident", "engaged"]
//                },
//                "keywordMatches": {
//                    "requiredSkills": ["Java", "Spring Boot", "Microservices"],
//                    "matchedSkills": ["Java", "Spring Boot"],
//                    "missingSkills": ["Kubernetes", "Docker"]
//                }
//            }
//            """);
//
//        return prompt.toString();
//    }
//
//    public String buildResumeAnalysisPrompt(
//            String resumeText,
//            String jobDescription,
//            List<String> requiredSkills) {
//
//        StringBuilder prompt = new StringBuilder();
//        prompt.append("Analyze the following resume against the job description and provide detailed evaluation:\n\n");
//
//        if (jobDescription != null && !jobDescription.trim().isEmpty()) {
//            prompt.append("JOB DESCRIPTION:\n").append(jobDescription).append("\n\n");
//        }
//
//        if (requiredSkills != null && !requiredSkills.isEmpty()) {
//            prompt.append("REQUIRED SKILLS:\n");
//            prompt.append(String.join(", ", requiredSkills)).append("\n\n");
//        }
//
//        prompt.append("RESUME CONTENT:\n").append(resumeText).append("\n\n");
//
//        prompt.append("""
//            Please provide analysis in the following JSON format:
//            {
//                "skills": ["Java", "Spring Boot", "REST API", "SQL"],
//                "experienceYears": 5,
//                "educationLevel": "Master's Degree",
//                "jobTitleMatches": {
//                    "matchScore": 0.85,
//                    "matchedTitles": ["Software Engineer", "Backend Developer"]
//                },
//                "skillGaps": {
//                    "missingSkills": ["Kubernetes", "AWS"],
//                    "weakAreas": ["Cloud deployment", "CI/CD"]
//                },
//                "overallScore": 78.5,
//                "summary": "Strong backend developer with 5 years experience...",
//                "extractedEntities": {
//                    "name": "John Doe",
//                    "email": "john@example.com",
//                    "phone": "+1234567890",
//                    "locations": ["New York", "Remote"],
//                    "companies": ["Google", "Microsoft"],
//                    "titles": ["Senior Developer", "Software Engineer"]
//                }
//            }
//            """);
//
//        return prompt.toString();
//    }
//
//    public String buildQuestionGenerationPrompt(
//            String jobDescription,
//            List<String> requiredSkills,
//            List<QuestionDifficulty> difficulties,
//            List<String> categories,
//            Integer numberOfQuestions) {
//
//        StringBuilder prompt = new StringBuilder();
//        prompt.append("Generate interview questions for the following position:\n\n");
//
//        prompt.append("JOB DESCRIPTION:\n").append(jobDescription).append("\n\n");
//
//        if (requiredSkills != null && !requiredSkills.isEmpty()) {
//            prompt.append("REQUIRED SKILLS:\n");
//            prompt.append(String.join(", ", requiredSkills)).append("\n\n");
//        }
//
//        prompt.append("Please generate ").append(numberOfQuestions).append(" interview questions with the following criteria:\n");
//
//        if (difficulties != null && !difficulties.isEmpty()) {
//            String difficultyStr = difficulties.stream()
//                    .map(QuestionDifficulty::name)
//                    .collect(Collectors.joining("/"));
//            prompt.append("- Difficulty levels: ").append(difficultyStr).append("\n");
//        }
//
//        if (categories != null && !categories.isEmpty()) {
//            prompt.append("- Categories: ").append(String.join(", ", categories)).append("\n");
//        }
//
//        prompt.append("""
//
//            Format each question as JSON object with these fields:
//            {
//                "questionText": "The interview question",
//                "category": "Technical/Behavioral/Cultural",
//                "difficulty": "EASY/MEDIUM/HARD",
//                "expectedAnswer": "Guidelines for expected answer",
//                "evaluationCriteria": {
//                    "keyPoints": ["Point 1", "Point 2"],
//                    "redFlags": ["Flag 1", "Flag 2"],
//                    "greenFlags": ["Flag 1", "Flag 2"]
//                },
//                "tags": ["tag1", "tag2"]
//            }
//
//            Return as a JSON array of questions.
//            """);
//
//        return prompt.toString();
//    }
//
//    public String buildSentimentAnalysisPrompt(String text, String context) {
//        StringBuilder prompt = new StringBuilder();
//        prompt.append("Analyze the sentiment of the following text");
//
//        if (context != null && !context.trim().isEmpty()) {
//            prompt.append(" in the context of: ").append(context);
//        }
//
//        prompt.append(":\n\n").append(text).append("\n\n");
//
//        prompt.append("""
//            Provide sentiment analysis in the following JSON format:
//            {
//                "sentiment": "POSITIVE/NEGATIVE/NEUTRAL",
//                "confidence": 0.85,
//                "emotionScores": {
//                    "joy": 0.7,
//                    "sadness": 0.1,
//                    "anger": 0.05,
//                    "fear": 0.05,
//                    "surprise": 0.1
//                },
//                "keyPhrases": ["phrase1", "phrase2"],
//                "summary": "Brief summary of sentiment"
//            }
//            """);
//
//        return prompt.toString();
//    }
//}
