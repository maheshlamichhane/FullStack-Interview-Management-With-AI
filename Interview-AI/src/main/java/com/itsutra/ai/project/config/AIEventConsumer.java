//package com.itsutra.ai.project.config;
//
//import com.itsutra.ai.project.service.AIService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class AIEventConsumer {
//
//    private final AIService aiService;
//
//    @KafkaListener(topics = "interview-started", groupId = "ai-service")
//    public void handleInterviewStarted(Map<String, Object> event) {
//        log.info("Received interview started event: {}", event);
//        // Trigger pre-interview analysis if needed
//    }
//
//    @KafkaListener(topics = "resume-uploaded", groupId = "ai-service")
//    public void handleResumeUploaded(Map<String, Object> event) {
//        log.info("Received resume uploaded event: {}", event);
//        // Auto-trigger resume analysis
//    }
//
//    @KafkaListener(topics = "job-posted", groupId = "ai-service")
//    public void handleJobPosted(Map<String, Object> event) {
//        log.info("Received job posted event: {}", event);
//        // Auto-generate interview questions for new job
//    }
//}
