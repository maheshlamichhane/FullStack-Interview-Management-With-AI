package com.itsutra.project.entity;

import com.itsutra.project.enums.ResponseStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "request_id", nullable = false)
    private AIRequest request;

    @Column(name = "response_data", columnDefinition = "TEXT")
    private String responseData;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResponseStatus status;

    @Column(name = "error_message")
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
