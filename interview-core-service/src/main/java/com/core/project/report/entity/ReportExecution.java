package com.core.project.report.entity;//package com.itsutra.project.report.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "report_executions")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ReportExecution {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "report_id", nullable = false)
//    private Report report;
//
//    @Column(name = "executed_at", nullable = false)
//    private LocalDateTime executedAt;
//
//    @Column(name = "completed_at")
//    private LocalDateTime completedAt;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "status", nullable = false)
//    private ExecutionStatus status;
//
//    @Column(name = "execution_time_ms")
//    private Long executionTimeMs;
//
//    @Column(name = "record_count")
//    private Long recordCount;
//
//    @Column(name = "error_message", columnDefinition = "TEXT")
//    private String errorMessage;
//
//    @Column(name = "parameters", columnDefinition = "TEXT")
//    private String parameters; // JSON string of parameters used
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    public enum ExecutionStatus {
//        RUNNING,
//        COMPLETED,
//        FAILED,
//        CANCELLED
//    }
//
//    @PrePersist
//    public void calculateExecutionTime() {
//        if (completedAt != null && executedAt != null) {
//            this.executionTimeMs = Duration.between(executedAt, completedAt).toMillis();
//        }
//    }
//}
