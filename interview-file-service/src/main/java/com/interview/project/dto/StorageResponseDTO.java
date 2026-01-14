package com.interview.project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StorageResponseDTO {
    private Long id;
    private String name;
    private String provider;
    private String bucketName;
    private String region;
    private String endpoint;
    private Boolean isDefault;
    private Boolean isActive;
    private Long maxFileSize;
    private String[] allowedExtensions;
    private Long quotaBytes;
    private Long usedBytes;
    private Double usagePercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
