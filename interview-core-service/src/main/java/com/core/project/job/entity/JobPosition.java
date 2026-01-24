package com.core.project.job.entity;//package com.itsutra.project.job.entity;

import com.core.project.job.enums.JobStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Table(name = "job_positions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPosition {

    @Id
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String code;

    private String description;

    private String responsibilities;

    private String requirements;

    private String benefits;

//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "department_id", nullable = false)
//    private Department department;


//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "location_id", nullable = false)
//    private Location location;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "employment_type", nullable = false)
//    private EmploymentType employmentType;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "experience_level", nullable = false)
//    private ExperienceLevel experienceLevel;

    private Double minSalary;


    private Double maxSalary;


    private String salaryCurrency;

    @Builder.Default
    private Integer openPositions = 1;

    @Builder.Default
    private Integer filledPositions = 0;

    @Builder.Default
    private JobStatus status = JobStatus.DRAFT;

    @Builder.Default
    private Boolean isRemote = false;

    @Builder.Default
    private Boolean isHybrid = false;

    private LocalDateTime publishedAt;

    private LocalDateTime closedAt;

    private LocalDateTime applicationDeadline;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

//    @OneToMany(mappedBy = "jobPosition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<JobRequirement> jobRequirements = new ArrayList<>();

//    @OneToMany(mappedBy = "jobPosition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<JobSkill> requiredSkills = new ArrayList<>();


    // Helper methods
    public Boolean isActive() {
        return status == JobStatus.PUBLISHED &&
                (applicationDeadline == null || applicationDeadline.isAfter(LocalDateTime.now()));
    }

    public Integer getRemainingPositions() {
        return openPositions - filledPositions;
    }

    public Boolean isAcceptingApplications() {
        return isActive() && getRemainingPositions() > 0;
    }
}
