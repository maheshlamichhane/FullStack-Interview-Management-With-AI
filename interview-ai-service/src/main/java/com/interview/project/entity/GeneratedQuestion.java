package com.interview.project.entity;

import com.interview.project.enums.QuestionDifficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "generated_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedQuestion extends BaseEntity {

    private String requestId;

    private UUID jobId;

    private String category;

    private QuestionDifficulty difficulty;

    private String questionText;

    private String expectedAnswer;

    private Map<String, Object> evaluationCriteria;

    private List<String> tags;

    private String modelUsed;

    private Map<String, Object> metadata;
}
