package com.core.project.job.entity;//package com.itsutra.project.job.entity;
//
//import com.itsutra.project.job.enums.EmploymentType;
//import com.itsutra.project.job.enums.ExperienceLevel;
//import com.itsutra.project.job.enums.JobStatus;
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
//@Table(name = "job_positions")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class JobPosition {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank
//    @Column(name = "title", nullable = false)
//    private String title;
//
//    @NotBlank
//    @Column(name = "code", nullable = false, unique = true)
//    private String code; // Internal job code like "DEV-001"
//
//    @Column(name = "description", columnDefinition = "TEXT")
//    private String description;
//
//    @Column(name = "responsibilities", columnDefinition = "TEXT")
//    private String responsibilities;
//
//    @Column(name = "requirements", columnDefinition = "TEXT")
//    private String requirements;
//
//    @Column(name = "benefits", columnDefinition = "TEXT")
//    private String benefits;
//
//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "department_id", nullable = false)
//    private Department department;
//
//
//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "location_id", nullable = false)
//    private Location location;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "employment_type", nullable = false)
//    private EmploymentType employmentType;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "experience_level", nullable = false)
//    private ExperienceLevel experienceLevel;
//
//    @Column(name = "min_salary")
//    private Double minSalary;
//
//    @Column(name = "max_salary")
//    private Double maxSalary;
//
//    @Column(name = "salary_currency")
//    private String salaryCurrency;
//
//    @Column(name = "open_positions")
//    @Builder.Default
//    private Integer openPositions = 1;
//
//    @Column(name = "filled_positions")
//    @Builder.Default
//    private Integer filledPositions = 0;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "status", nullable = false)
//    @Builder.Default
//    private JobStatus status = JobStatus.DRAFT;
//
//    @Column(name = "is_remote")
//    @Builder.Default
//    private Boolean isRemote = false;
//
//    @Column(name = "is_hybrid")
//    @Builder.Default
//    private Boolean isHybrid = false;
//
//    @Column(name = "published_at")
//    private LocalDateTime publishedAt;
//
//    @Column(name = "closed_at")
//    private LocalDateTime closedAt;
//
//    @Column(name = "application_deadline")
//    private LocalDateTime applicationDeadline;
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @OneToMany(mappedBy = "jobPosition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<JobRequirement> jobRequirements = new ArrayList<>();
//
//    @OneToMany(mappedBy = "jobPosition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<JobSkill> requiredSkills = new ArrayList<>();
//
//
//
//
//    // Helper methods
//    public Boolean isActive() {
//        return status == JobStatus.PUBLISHED &&
//                (applicationDeadline == null || applicationDeadline.isAfter(LocalDateTime.now()));
//    }
//
//    public Integer getRemainingPositions() {
//        return openPositions - filledPositions;
//    }
//
//    public Boolean isAcceptingApplications() {
//        return isActive() && getRemainingPositions() > 0;
//    }
//}
