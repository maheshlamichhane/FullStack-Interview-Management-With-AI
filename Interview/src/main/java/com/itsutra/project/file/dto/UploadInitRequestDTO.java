package com.itsutra.project.file.dto;

import com.itsutra.project.file.enums.FileCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class UploadInitRequestDTO {

    @NotBlank(message = "File name is required")
    private String fileName;

    @NotNull(message = "File size is required")
    private Long fileSize;

    private String mimeType;
    private FileCategory category;
    private String description;
    private Long chunkSize;
    private Long uploadedById;
    private Map<String, Object> metadata;
}
