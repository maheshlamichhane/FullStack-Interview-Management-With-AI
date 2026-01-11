package com.interview.project.file.entity;//package com.itsutra.project.file.entity;
//
//import com.itsutra.project.file.enums.UploadStatus;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "upload_sessions")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UploadSession {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank
//    @Column(name = "session_id", nullable = false, unique = true)
//    private String sessionId;
//
//    @NotBlank
//    @Column(name = "upload_id", nullable = false)
//    private String uploadId; // External upload identifier
//
//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "file_id", nullable = false)
//    private File file;
//
//    @Column(name = "chunk_size")
//    private Long chunkSize;
//
//    @Column(name = "total_chunks")
//    private Integer totalChunks;
//
//    @Column(name = "uploaded_chunks")
//    @Builder.Default
//    private Integer uploadedChunks = 0;
//
//    @Column(name = "total_size")
//    private Long totalSize;
//
//    @Column(name = "uploaded_size")
//    @Builder.Default
//    private Long uploadedSize = 0L;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "status", nullable = false)
//    @Builder.Default
//    private UploadStatus status = UploadStatus.INITIATED;
//
//    @Column(name = "expires_at")
//    private LocalDateTime expiresAt;
//
//    @Column(name = "metadata", columnDefinition = "TEXT")
//    private String metadata; // JSON metadata
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @Column(name = "completed_at")
//    private LocalDateTime completedAt;
//
//    @Column(name = "error_message")
//    private String errorMessage;
//
//
//    // Helper methods
//    public Double getProgressPercentage() {
//        if (totalSize == null || totalSize == 0) return 0.0;
//        return (uploadedSize.doubleValue() / totalSize.doubleValue()) * 100;
//    }
//
//    public Boolean isExpired() {
//        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
//    }
//
//    public Boolean isCompleted() {
//        return status == UploadStatus.COMPLETED;
//    }
//
//    public Boolean canResume() {
//        return (status == UploadStatus.IN_PROGRESS || status == UploadStatus.FAILED) && !isExpired();
//    }
//}
