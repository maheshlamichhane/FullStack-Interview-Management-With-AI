package com.interview.project.file.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class UploadInitResponseDTO {
    private String sessionId;
    private String uploadId;
    private Long chunkSize;
    private Integer totalChunks;
    private LocalDateTime expiresAt;
    private Map<String, String> uploadUrls; // For chunked uploads
}
