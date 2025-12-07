package com.itsutra.project.file.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class UploadCompleteRequestDTO {
    @NotBlank(message = "Session ID is required")
    private String sessionId;

    private String checksum;
    private Map<String, Object> finalMetadata;
}
