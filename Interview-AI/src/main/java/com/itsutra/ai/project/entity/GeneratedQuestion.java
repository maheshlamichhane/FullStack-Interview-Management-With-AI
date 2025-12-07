package com.itsutra.ai.project.entity;

import com.itsutra.ai.project.enums.QuestionDifficulty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "generated_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedQuestion extends BaseEntity {

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "job_id")
    private UUID jobId;

    @Column(name = "category")
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private QuestionDifficulty difficulty;

    @Column(name = "question_text", columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @Column(name = "expected_answer", columnDefinition = "TEXT")
    private String expectedAnswer;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "evaluation_criteria", columnDefinition = "jsonb")
    private Map<String, Object> evaluationCriteria;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "tags", columnDefinition = "text[]")
    private List<String> tags;

    @Column(name = "model_used")
    private String modelUsed;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;
}
