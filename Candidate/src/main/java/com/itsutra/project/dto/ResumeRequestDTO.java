package com.itsutra.project.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResumeRequestDTO {
    private Long candidateId;
    private MultipartFile file;
    private Boolean isPrimary;
}
