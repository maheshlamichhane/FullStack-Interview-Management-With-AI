package com.itsutra.project.file.entity;


import com.itsutra.project.common.entity.User;
import com.itsutra.project.file.enums.DocumentCategory;
import com.itsutra.project.file.enums.DocumentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @NotBlank
    @Column(name = "document_type", nullable = false)
    private String documentType; // RESUME, CONTRACT, OFFER_LETTER, etc.

    @Enumerated(EnumType.STRING)
    @Column(name = "document_category", nullable = false)
    private DocumentCategory category;

    @Column(name = "tags")
    private String tags; // Comma-separated tags

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON metadata

    @Column(name = "is_confidential")
    @Builder.Default
    private Boolean isConfidential = false;

    @Column(name = "is_verified")
    @Builder.Default
    private Boolean isVerified = false;

    @Column(name = "verified_by")
    private Long verifiedBy;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "version")
    @Builder.Default
    private Integer version = 1;

    @Column(name = "parent_document_id")
    private Long parentDocumentId; // For document versions

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private DocumentStatus status = DocumentStatus.DRAFT;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DocumentShare> shares = new ArrayList<>();

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DocumentAccessLog> accessLogs = new ArrayList<>();


    // Helper methods
    public Boolean hasParent() {
        return parentDocumentId != null;
    }

    public Boolean isLatestVersion() {
        // This would check if this is the latest version
        return true; // Simplified
    }
}