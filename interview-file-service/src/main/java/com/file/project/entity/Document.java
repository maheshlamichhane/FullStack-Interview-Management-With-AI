package com.file.project.entity;


import com.file.project.enums.DocumentCategory;
import com.file.project.enums.DocumentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    private Long id;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String documentType;

    private DocumentCategory category;

    private String tags;

    private String metadata;


    @Builder.Default
    private Boolean isConfidential = false;

    @Builder.Default
    private Boolean isVerified = false;

    private Long verifiedBy;

    private LocalDateTime verifiedAt;


    @Builder.Default
    private Integer version = 1;

    private Long parentDocumentId; // For document versions


    @Builder.Default
    private DocumentStatus status = DocumentStatus.DRAFT;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // Helper methods
    public Boolean hasParent() {
        return parentDocumentId != null;
    }

    public Boolean isLatestVersion() {
        // This would check if this is the latest version
        return true; // Simplified
    }
}