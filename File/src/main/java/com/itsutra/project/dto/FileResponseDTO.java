package com.itsutra.project.dto;

import com.itsutra.project.enums.FileCategory;
import com.itsutra.project.enums.FileStatus;
import com.itsutra.project.enums.StorageProvider;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileResponseDTO {
    private Long id;
    private String name;
    private String originalName;
    private String storageKey;
    private String description;
    private String mimeType;
    private Long size;
    private String formattedSize;
    private String extension;
    private FileCategory category;
    private FileStatus status;
    private StorageProvider storageProvider;
    private String storagePath;
    private Integer version;
    private Boolean isEncrypted;
    private Integer retentionPeriod;
    private LocalDateTime expiresAt;
    private LocalDateTime accessedAt;
    private Long accessCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserInfoDTO uploadedBy;
    private String downloadUrl;
    private String previewUrl;
    private Boolean isExpired;
}
