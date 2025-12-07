package com.itsutra.project.file.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileDownloadResponseDTO {
    private String downloadUrl;
    private String fileName;
    private String mimeType;
    private Long fileSize;
    private LocalDateTime expiresAt;
    private Boolean requiresAuthentication;
}
