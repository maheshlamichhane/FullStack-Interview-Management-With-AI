package com.itsutra.project.entity;

import com.itsutra.project.enums.PermissionLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dashboard_shares")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id", nullable = false)
    private Dashboard dashboard;

    @Column(name = "shared_with_user_id")
    private Long sharedWithUserId;

    @Column(name = "shared_with_role")
    private String sharedWithRole;

    @Column(name = "permission_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionLevel permissionLevel;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
