package com.itsutra.project.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dashboard_widgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardWidget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id", nullable = false)
    private Dashboard dashboard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visualization_id")
    private Visualization visualization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metric_id")
    private Metric metric;

    @Column(name = "widget_type", nullable = false)
    private String widgetType; // VISUALIZATION, METRIC, TEXT

    @Column(name = "title")
    private String title;

    @Column(name = "position_x")
    private Integer positionX;

    @Column(name = "position_y")
    private Integer positionY;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "config", columnDefinition = "TEXT")
    private String config; // JSON configuration
}
