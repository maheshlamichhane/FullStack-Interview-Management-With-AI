package com.itsutra.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "module", length = 50)
    private String module;

    public enum PermissionName {
        // User Management
        USER_CREATE, USER_READ, USER_UPDATE, USER_DELETE,
        USER_ACTIVATE, USER_DEACTIVATE,

        // Role Management
        ROLE_CREATE, ROLE_READ, ROLE_UPDATE, ROLE_DELETE,

        // Interview Management
        INTERVIEW_SCHEDULE, INTERVIEW_VIEW, INTERVIEW_UPDATE, INTERVIEW_CANCEL,
        INTERVIEW_FEEDBACK_CREATE, INTERVIEW_FEEDBACK_VIEW,

        // Candidate Management
        CANDIDATE_CREATE, CANDIDATE_READ, CANDIDATE_UPDATE, CANDIDATE_DELETE,

        // Job Management
        JOB_CREATE, JOB_READ, JOB_UPDATE, JOB_DELETE,

        // Analytics
        ANALYTICS_VIEW, REPORTS_GENERATE
    }
}
