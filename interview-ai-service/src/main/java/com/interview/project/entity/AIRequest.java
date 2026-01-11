package com.interview.project.entity;

import com.interview.project.enums.AIServiceType;
import com.interview.project.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;


@Table(name = "ai_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AIRequest extends BaseEntity {

    private String requestId;

    private AIServiceType serviceType;

    private UUID userId;


    private UUID candidateId;

    private UUID interviewId;

    private String inputData;

    private String outputData;

    private String modelUsed;

    private Integer tokensUsed;

    private Long processingTimeMs;

    private BigDecimal cost;

    private RequestStatus status = RequestStatus.PENDING;

    private String errorMessage;

    private Map<String, Object> metadata;
}
