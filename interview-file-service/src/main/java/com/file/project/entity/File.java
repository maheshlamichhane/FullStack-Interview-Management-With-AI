package com.file.project.entity;//package com.itsutra.project.file.entity;
//
//
//import com.itsutra.project.common.entity.User;
//import com.itsutra.project.file.enums.FileCategory;
//import com.itsutra.project.file.enums.FileStatus;
//import com.itsutra.project.file.enums.StorageProvider;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "files")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class File {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank
//    @Column(name = "name", nullable = false)
//    private String name;
//
//    @NotBlank
//    @Column(name = "original_name", nullable = false)
//    private String originalName;
//
//    @NotBlank
//    @Column(name = "storage_key", nullable = false, unique = true)
//    private String storageKey; // Unique identifier in storage system
//
//    @Column(name = "description")
//    private String description;
//
//    @NotBlank
//    @Column(name = "mime_type", nullable = false)
//    private String mimeType;
//
//    @NotNull
//    @Column(name = "size", nullable = false)
//    private Long size; // Size in bytes
//
//    @Column(name = "extension")
//    private String extension;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "category", nullable = false)
//    private FileCategory category;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "status", nullable = false)
//    @Builder.Default
//    private FileStatus status = FileStatus.ACTIVE;
//
//    @Column(name = "checksum")
//    private String checksum; // MD5 or SHA256 checksum
//
//    @Column(name = "storage_provider")
//    @Enumerated(EnumType.STRING)
//    @Builder.Default
//    private StorageProvider storageProvider = StorageProvider.LOCAL;
//
//    @Column(name = "storage_path")
//    private String storagePath;
//
//    @Column(name = "version")
//    @Builder.Default
//    private Integer version = 1;
//
//    @Column(name = "is_encrypted")
//    @Builder.Default
//    private Boolean isEncrypted = false;
//
//    @Column(name = "encryption_key")
//    private String encryptionKey;
//
//    @Column(name = "retention_period")
//    private Integer retentionPeriod; // in days
//
//    @Column(name = "expires_at")
//    private LocalDateTime expiresAt;
//
//    @Column(name = "accessed_at")
//    private LocalDateTime accessedAt;
//
//    @Column(name = "access_count")
//    @Builder.Default
//    private Long accessCount = 0L;
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "uploaded_by")
//    private User uploadedBy;
//
//    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<Document> documents = new ArrayList<>();
//
//    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<UploadSession> uploadSessions = new ArrayList<>();
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "created_by", nullable = false)
//    private User createdBy;
//
//
//
//    // Helper methods
//    public String getFormattedSize() {
//        if (size < 1024) return size + " B";
//        else if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
//        else if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024.0));
//        else return String.format("%.1f GB", size / (1024.0 * 1024.0 * 1024.0));
//    }
//
//    public Boolean isExpired() {
//        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
//    }
//
//    public Boolean isImage() {
//        return mimeType != null && mimeType.startsWith("image/");
//    }
//
//    public Boolean isVideo() {
//        return mimeType != null && mimeType.startsWith("video/");
//    }
//
//    public Boolean isDocument() {
//        return mimeType != null && (
//                mimeType.startsWith("application/pdf") ||
//                        mimeType.contains("word") ||
//                        mimeType.contains("excel") ||
//                        mimeType.contains("powerpoint") ||
//                        mimeType.contains("text/")
//        );
//    }
//
//    public void incrementAccessCount() {
//        this.accessCount++;
//        this.accessedAt = LocalDateTime.now();
//    }
//}
