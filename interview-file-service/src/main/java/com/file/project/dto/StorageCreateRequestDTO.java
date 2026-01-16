package com.file.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class StorageCreateRequestDTO {


    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Provider is required")
    private String provider;

    private String bucketName;
    private String region;
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private Boolean isDefault;
    private Long maxFileSize;
    private String allowedExtensions;
    private Long quotaBytes;
    private Map<String, Object> config;
}
