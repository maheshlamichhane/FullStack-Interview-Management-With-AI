package com.interview.project.dto;

import com.interview.project.enums.DocumentCategory;
import com.interview.project.enums.DocumentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class DocumentResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String documentType;
    private DocumentCategory category;
    private List<String> tags;
    private Map<String, Object> metadata;
    private Boolean isConfidential;
    private Boolean isVerified;
    private Long verifiedBy;
    private LocalDateTime verifiedAt;
    private Integer version;
    private Long parentDocumentId;
    private DocumentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private FileResponseDTO file;
    private UserInfoDTO createdBy;
    private List<DocumentShareResponseDTO> shares;
    private Long accessCount;
}
