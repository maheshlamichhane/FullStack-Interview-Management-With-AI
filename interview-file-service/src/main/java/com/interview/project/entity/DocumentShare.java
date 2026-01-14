package com.interview.project.entity;//package com.itsutra.project.file.entity;
//
//
//import com.itsutra.project.common.entity.User;
//import com.itsutra.project.file.enums.PermissionLevel;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "document_shares")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class DocumentShare {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "document_id", nullable = false)
//    private Document document;
//
//    @Column(name = "shared_with_user_id")
//    private Long sharedWithUserId;
//
//    @Column(name = "shared_with_role")
//    private String sharedWithRole;
//
//    @Column(name = "shared_with_email")
//    private String sharedWithEmail;
//
//    @Column(name = "permission_level", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private PermissionLevel permissionLevel;
//
//    @Column(name = "access_code")
//    private String accessCode; // For public sharing
//
//    @Column(name = "expires_at")
//    private LocalDateTime expiresAt;
//
//    @Column(name = "is_active")
//    @Builder.Default
//    private Boolean isActive = true;
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shared_by")
//    private User sharedBy;
//
//
//    public Boolean isExpired() {
//        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
//    }
//
//    public Boolean isValid() {
//        return isActive && !isExpired();
//    }
//}
