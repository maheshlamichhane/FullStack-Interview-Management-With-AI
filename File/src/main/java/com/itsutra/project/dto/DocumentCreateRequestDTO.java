package com.itsutra.project.dto;

import com.itsutra.project.enums.DocumentCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DocumentCreateRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Document type is required")
    private String documentType;

    @NotNull(message = "Category is required")
    private DocumentCategory category;

    private List<String> tags;
    private Map<String, Object> metadata;
    private Boolean isConfidential;
    private Long fileId;
    private Long createdById;
}
