package com.itsutra.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public  class JobDashboardStatsDTO {
    private Long totalPositions;
    private Long activePositions;
    private Long draftPositions;
    private Long closedPositions;
    private Long expiredPositions;
}
