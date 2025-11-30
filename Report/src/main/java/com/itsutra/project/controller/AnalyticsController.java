package com.itsutra.project.controller;

import com.itsutra.project.dto.AnalyticsOverviewResponseDTO;
import com.itsutra.project.dto.ExportRequestDTO;
import com.itsutra.project.service.AnalyticsOverviewService;
import com.itsutra.project.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsOverviewService analyticsOverviewService;
    private final ExportService exportService;

    @GetMapping("/overview")
    public ResponseEntity<AnalyticsOverviewResponseDTO> getAnalyticsOverview() {
        AnalyticsOverviewResponseDTO overview = analyticsOverviewService.getAnalyticsOverview();
        return ResponseEntity.ok(overview);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAnalyticsStats() {
        Map<String, Object> stats = analyticsOverviewService.getAnalyticsStats();
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/export/report/{reportId}")
    public ResponseEntity<Resource> exportReport(
            @PathVariable Long reportId,
            @RequestBody ExportRequestDTO request) {

        Resource exportFile = exportService.exportReport(reportId, request);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + getExportFileName(reportId, request.getFormat()) + "\"")
                .body(exportFile);
    }

    @PostMapping("/export/dashboard/{dashboardId}")
    public ResponseEntity<Resource> exportDashboard(
            @PathVariable Long dashboardId,
            @RequestBody ExportRequestDTO request) {

        Resource exportFile = exportService.exportDashboard(dashboardId, request);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + getExportFileName(dashboardId, request.getFormat()) + "\"")
                .body(exportFile);
    }

    private String getExportFileName(Long id, String format) {
        return "export_" + id + "_" + System.currentTimeMillis() + "." + format.toLowerCase();
    }
}
