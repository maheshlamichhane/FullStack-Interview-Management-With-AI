package com.interview.project.file.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StorageQuotaUpdateRequestDTO {
    @NotNull(message = "Quota bytes is required")
    private Long quotaBytes;
}
