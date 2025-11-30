package com.itsutra.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class DocumentVersionRequestDTO {

    @NotNull(message = "File ID is required")
    private Long fileId;

    private String description;
    private Map<String, Object> metadata;
}
