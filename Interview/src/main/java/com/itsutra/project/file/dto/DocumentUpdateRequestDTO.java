package com.itsutra.project.file.dto;

import com.itsutra.project.file.enums.DocumentCategory;
import com.itsutra.project.file.enums.DocumentStatus;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DocumentUpdateRequestDTO {
    private String title;
    private String description;
    private String documentType;
    private DocumentCategory category;
    private List<String> tags;
    private Map<String, Object> metadata;
    private Boolean isConfidential;
    private DocumentStatus status;
}
