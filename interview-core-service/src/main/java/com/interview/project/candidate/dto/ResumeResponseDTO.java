package com.interview.project.candidate.dto;


import com.interview.project.candidate.enums.ParsingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResumeResponseDTO {
    private Long id;
    private Long candidateId;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private Integer version;
    private Boolean isPrimary;
    private String parsedContent;
    private ParsingStatus parsingStatus;
    private String parsedSkills;
    private Double parsedExperience;
    private LocalDateTime uploadedAt;
}
