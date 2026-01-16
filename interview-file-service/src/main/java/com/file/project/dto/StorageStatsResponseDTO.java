package com.file.project.dto;

import lombok.Data;

import java.util.Map;

@Data
public class StorageStatsResponseDTO {
    private Long totalFiles;
    private Long totalStorageUsed;
    private Long totalQuota;
    private Double overallUsagePercentage;
    private Map<String, StorageCategoryStatsDTO> categoryStats;
    private Map<String, Long> providerDistribution;

    @Data
    public static class StorageCategoryStatsDTO {
        private Long fileCount;
        private Long storageUsed;
        private Double percentage;
    }
}
