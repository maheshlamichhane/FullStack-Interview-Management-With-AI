package com.itsutra.project.file.entity;

import com.itsutra.project.common.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "storage_configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "provider", nullable = false)
    private String provider; // AWS_S3, AZURE_BLOB, etc.

    @Column(name = "bucket_name")
    private String bucketName;

    @Column(name = "region")
    private String region;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "access_key")
    private String accessKey; // Encrypted

    @Column(name = "secret_key")
    private String secretKey; // Encrypted

    @Column(name = "is_default")
    @Builder.Default
    private Boolean isDefault = false;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "max_file_size")
    private Long maxFileSize; // in bytes

    @Column(name = "allowed_extensions")
    private String allowedExtensions; // Comma-separated

    @Column(name = "quota_bytes")
    private Long quotaBytes;

    @Column(name = "used_bytes")
    @Builder.Default
    private Long usedBytes = 0L;

    @Column(name = "config", columnDefinition = "TEXT")
    private String config; // JSON configuration

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // Helper methods
    public Double getUsagePercentage() {
        if (quotaBytes == null || quotaBytes == 0) return 0.0;
        return (usedBytes.doubleValue() / quotaBytes.doubleValue()) * 100;
    }

    public Boolean hasQuotaAvailable(Long fileSize) {
        if (quotaBytes == null) return true;
        return (usedBytes + fileSize) <= quotaBytes;
    }

    public String[] getAllowedExtensionsArray() {
        return allowedExtensions != null ? allowedExtensions.split(",") : new String[0];
    }
}