package com.itsutra.project.report.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "metric_values")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metric_id", nullable = false)
    private Metric metric;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    @Column(name = "time_period")
    private String timePeriod; // DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY

    @Column(name = "dimension_filters", columnDefinition = "TEXT")
    private String dimensionFilters; // JSON filters applied

    @Column(name = "previous_value")
    private Double previousValue;

    @Column(name = "change_percentage")
    private Double changePercentage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
