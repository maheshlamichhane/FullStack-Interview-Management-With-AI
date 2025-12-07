package com.itsutra.project.file.dto;

import com.itsutra.project.file.enums.UploadStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UploadStatusResponseDTO {
    private String sessionId;
    private String uploadId;
    private UploadStatus status;
    private Integer uploadedChunks;
    private Integer totalChunks;
    private Long uploadedSize;
    private Long totalSize;
    private Double progressPercentage;
    private LocalDateTime expiresAt;
    private String errorMessage;
}
