package com.itsutra.project.service;

import com.itsutra.project.dao.ResumeDAO;
import com.itsutra.project.entity.Resume;
import com.itsutra.project.enums.ParsingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeParserService {

    private final ResumeDAO resumeDAO;
    private final ObjectMapper objectMapper;

    @Async
    public void parseResumeAsync(Resume resume) {
        try {
            log.info("Starting resume parsing for: {}", resume.getFileName());

            // Update status to processing
            resume.setParsingStatus(ParsingStatus.PROCESSING);
            resumeDAO.save(resume);

            // Read file content
            String content = extractTextFromFile(resume.getFilePath());

            // Parse resume content (simplified implementation)
            List<String> skills = extractSkills(content);
            Double experience = extractExperience(content);

            // Update resume with parsed data
            resume.setParsedContent(content);
            resume.setParsedSkills(objectMapper.writeValueAsString(skills));
            resume.setParsedExperience(experience);
            resume.setParsingStatus(ParsingStatus.COMPLETED);

            resumeDAO.save(resume);
            log.info("Completed resume parsing for: {}", resume.getFileName());

        } catch (Exception e) {
            log.error("Failed to parse resume: {}", resume.getFileName(), e);
            resume.setParsingStatus(ParsingStatus.FAILED);
            resumeDAO.save(resume);
        }
    }

    private String extractTextFromFile(String filePath) throws IOException {
        // Simplified text extraction
        // In real implementation, use libraries like Apache Tika for PDF/DOC parsing
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }

    private List<String> extractSkills(String content) {
        // Simplified skill extraction
        // In real implementation, use NLP or pattern matching
        List<String> commonSkills = Arrays.asList(
                "Java", "Spring Boot", "Microservices", "Docker", "Kubernetes",
                "AWS", "React", "Angular", "Python", "JavaScript", "SQL", "NoSQL"
        );

        return commonSkills.stream()
                .filter(skill -> content.toLowerCase().contains(skill.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
    }

    private Double extractExperience(String content) {
        // Simplified experience extraction
        // In real implementation, use more sophisticated parsing
        if (content.toLowerCase().contains("years") || content.toLowerCase().contains("experience")) {
            // Extract numbers near experience keywords
            return 3.5; // Mock value
        }
        return 0.0;
    }
}