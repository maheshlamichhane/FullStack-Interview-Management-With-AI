package com.itsutra.project.entity;


import com.itsutra.project.dto.ReportColumnDTO;
import com.itsutra.project.dto.ReportParameterDTO;
import com.itsutra.project.enums.ReportCategory;
import com.itsutra.project.enums.ReportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotBlank
    @Column(name = "code", nullable = false, unique = true)
    private String code; // Internal report code like "HIRING_FUNNEL"

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ReportCategory category;

    @Column(name = "sql_query", columnDefinition = "TEXT")
    private String sqlQuery;

    @Column(name = "data_source")
    private String dataSource;

    @ElementCollection
    @CollectionTable(name = "report_parameters", joinColumns = @JoinColumn(name = "report_id"))
    @Builder.Default
    private List<ReportParameterDTO> parameters = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "report_columns", joinColumns = @JoinColumn(name = "report_id"))
    @Builder.Default
    private List<ReportColumnDTO> columns = new ArrayList<>();

    @Column(name = "refresh_frequency")
    private Integer refreshFrequency; // in minutes

    @Column(name = "is_scheduled")
    @Builder.Default
    private Boolean isScheduled = false;

    @Column(name = "is_public")
    @Builder.Default
    private Boolean isPublic = false;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "cache_duration")
    @Builder.Default
    private Integer cacheDuration = 30; // in minutes

    @Column(name = "last_run_at")
    private LocalDateTime lastRunAt;

    @Column(name = "next_run_at")
    private LocalDateTime nextRunAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ReportExecution> executions = new ArrayList<>();

    @ManyToMany(mappedBy = "reports")
    @Builder.Default
    private List<Dashboard> dashboards = new ArrayList<>();
}
