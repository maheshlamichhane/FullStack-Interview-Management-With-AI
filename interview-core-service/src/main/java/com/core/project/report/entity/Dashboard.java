package com.core.project.report.entity;//package com.itsutra.project.report.entity;
//
//
//import com.itsutra.project.common.entity.User;
//import com.itsutra.project.report.enums.DashboardCategory;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
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
//@Table(name = "dashboards")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Dashboard {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank
//    @Column(name = "name", nullable = false)
//    private String name;
//
//    @Column(name = "description")
//    private String description;
//
//    @NotBlank
//    @Column(name = "code", nullable = false, unique = true)
//    private String code;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "category", nullable = false)
//    private DashboardCategory category;
//
//    @Column(name = "layout_config", columnDefinition = "TEXT")
//    private String layoutConfig; // JSON configuration for widget layout
//
//    @Column(name = "is_public")
//    @Builder.Default
//    private Boolean isPublic = false;
//
//    @Column(name = "is_active")
//    @Builder.Default
//    private Boolean isActive = true;
//
//    @Column(name = "refresh_interval")
//    @Builder.Default
//    private Integer refreshInterval = 15;
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "created_by")
//    private User createdBy;
//
//    @ManyToMany
//    @JoinTable(
//            name = "dashboard_reports",
//            joinColumns = @JoinColumn(name = "dashboard_id"),
//            inverseJoinColumns = @JoinColumn(name = "report_id")
//    )
//    @Builder.Default
//    private List<Report> reports = new ArrayList<>();
//
//    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<DashboardWidget> widgets = new ArrayList<>();
//
//    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<DashboardShare> shares = new ArrayList<>();
//
//}
