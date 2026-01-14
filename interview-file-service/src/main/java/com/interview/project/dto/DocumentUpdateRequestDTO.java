package com.interview.project.dto;

import com.interview.project.enums.DocumentCategory;
import com.interview.project.enums.DocumentStatus;
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
