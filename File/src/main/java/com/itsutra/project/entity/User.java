package com.itsutra.project.entity;

import com.itsutra.project.enums.Department;
import com.itsutra.project.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "display_name")
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "department")
    private Department department;

    @Column(name = "title")
    private String title;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "locale")
    private String locale;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "is_verified")
    @Builder.Default
    private Boolean isVerified = false;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
//    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<Report> createdReports = new ArrayList<>();
//
//    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<Dashboard> createdDashboards = new ArrayList<>();

    // Helper methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Boolean hasRole(UserRole requiredRole) {
        return this.role == requiredRole;
    }

    public Boolean isAdmin() {
        return this.role == UserRole.ADMIN || this.role == UserRole.SUPER_ADMIN;
    }

//    public Boolean canEditReport(Report report) {
//        return isAdmin() || report.getCreatedBy().getId().equals(this.id);
//    }
//
//    public Boolean canViewDashboard(Dashboard dashboard) {
//        return dashboard.getIsPublic() ||
//                isAdmin() ||
//                dashboard.getCreatedBy().getId().equals(this.id) ||
//                dashboard.getShares().stream().anyMatch(share ->
//                        share.getSharedWithUserId().equals(this.id) ||
//                                (share.getSharedWithRole() != null && share.getSharedWithRole().equals(this.role.name()))
//                );
//    }
}