package com.core.project.report.entity;//package com.itsutra.project.report.entity;
//
//
//import com.itsutra.project.report.enums.VisualizationType;
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
//
//@Entity
//@Table(name = "visualizations")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Visualization {
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
//    @Enumerated(EnumType.STRING)
//    @Column(name = "type", nullable = false)
//    private VisualizationType type;
//
//    @Column(name = "config", columnDefinition = "TEXT")
//    private String config; // JSON configuration for the visualization
//
//    @Column(name = "data_query", columnDefinition = "TEXT")
//    private String dataQuery;
//
//    @Column(name = "width")
//    @Builder.Default
//    private Integer width = 400;
//
//    @Column(name = "height")
//    @Builder.Default
//    private Integer height = 300;
//
//    @Column(name = "is_interactive")
//    @Builder.Default
//    private Boolean isInteractive = false;
//
//    @Column(name = "refresh_interval")
//    private Integer refreshInterval; // in minutes
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
//    @JoinColumn(name = "report_id")
//    private Report report;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "dashboard_id")
//    private Dashboard dashboard;
//
//}
