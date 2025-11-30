package com.itsutra.project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileConversionResponseDTO {
    private String originalFileId;
    private String convertedFileId;
    private String targetFormat;
    private String downloadUrl;
    private Long fileSize;
    private LocalDateTime convertedAt;
    private String status;
}
