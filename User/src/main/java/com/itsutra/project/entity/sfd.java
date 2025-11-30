package com.itsutra.project.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true, nullable = false)
    private RoleName name;

    @Column(length = 200)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();

    public enum RoleName {
        ROLE_SUPER_ADMIN,    // Full system access
        ROLE_ADMIN,          // Administrative functions
        ROLE_RECRUITER,      // Can manage candidates and interviews
        ROLE_INTERVIEWER,    // Can conduct interviews
        ROLE_CANDIDATE,      // Applies for positions and participates in interviews
        ROLE_HIRING_MANAGER  // Makes final hiring decisions
    }
}
