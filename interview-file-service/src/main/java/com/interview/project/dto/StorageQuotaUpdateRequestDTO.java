package com.interview.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StorageQuotaUpdateRequestDTO {
    @NotNull(message = "Quota bytes is required")
    private Long quotaBytes;
}
