package com.core.project.report.entity;//package com.itsutra.project.report.entity;
//
//
//
//import com.itsutra.project.common.entity.User;
//import com.itsutra.project.report.enums.DataType;
//import com.itsutra.project.report.enums.MetricCategory;
//import com.itsutra.project.report.enums.TrendDirection;
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
//@Table(name = "metrics")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Metric {
//
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
//    private MetricCategory category;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "data_type", nullable = false)
//    private DataType dataType;
//
//    @Column(name = "calculation_formula", columnDefinition = "TEXT")
//    private String calculationFormula;
//
//    @Column(name = "data_source")
//    private String dataSource;
//
//    @Column(name = "aggregation_type")
//    private String aggregationType; // SUM, AVG, COUNT, MIN, MAX
//
//    @Column(name = "unit")
//    private String unit; // days, percentage, currency
//
//    @Column(name = "target_value")
//    private Double targetValue;
//
//    @Column(name = "warning_threshold")
//    private Double warningThreshold;
//
//    @Column(name = "critical_threshold")
//    private Double criticalThreshold;
//
//    @Column(name = "is_trend_available")
//    @Builder.Default
//    private Boolean isTrendAvailable = false;
//
//    @Column(name = "trend_direction")
//    @Enumerated(EnumType.STRING)
//    private TrendDirection trendDirection;
//
//    @Column(name = "is_active")
//    @Builder.Default
//    private Boolean isActive = true;
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @OneToMany(mappedBy = "metric", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<MetricValue> values = new ArrayList<>();
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "created_by")
//    private User createdBy;
//
//}
