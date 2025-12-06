package com.itsutra.project.report.controller;

import com.itsutra.project.report.dto.*;
import com.itsutra.project.report.service.DashboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboards")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping
    public ResponseEntity<DashboardResponseDTO> createDashboard(@Valid @RequestBody DashboardRequestDTO request) {
        DashboardResponseDTO response = dashboardService.createDashboard(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DashboardResponseDTO>> getAllDashboards() {
        List<DashboardResponseDTO> dashboards = dashboardService.getAllDashboards();
        return ResponseEntity.ok(dashboards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DashboardResponseDTO> getDashboardById(@PathVariable Long id) {
        DashboardResponseDTO response = dashboardService.getDashboardById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DashboardResponseDTO> updateDashboard(
            @PathVariable Long id,
            @Valid @RequestBody DashboardRequestDTO request) {

        DashboardResponseDTO response = dashboardService.updateDashboard(id, request);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/{id}/data")
    public ResponseEntity<Map<String, Object>> getDashboardData(@PathVariable Long id) {
        Map<String, Object> data = dashboardService.getDashboardData(id);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/{id}/widgets")
    public ResponseEntity<DashboardWidgetResponseDTO> addWidgetToDashboard(
            @PathVariable Long id,
            @Valid @RequestBody DashboardWidgetRequestDTO request) {

        DashboardWidgetResponseDTO response = dashboardService.addWidgetToDashboard(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/shares")
    public ResponseEntity<DashboardShareResponseDTO> shareDashboard(
            @PathVariable Long id,
            @Valid @RequestBody DashboardShareRequestDTO request) {

        DashboardShareResponseDTO response = dashboardService.shareDashboard(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/shares/{shareId}")
    public ResponseEntity<Void> revokeDashboardShare(
            @PathVariable Long id,
            @PathVariable Long shareId) {

        dashboardService.revokeDashboardShare(id, shareId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/widgets/{widgetId}")
    public ResponseEntity<Void> removeWidgetFromDashboard(
            @PathVariable Long id,
            @PathVariable Long widgetId) {

        dashboardService.removeWidgetFromDashboard(id, widgetId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDashboard(@PathVariable Long id) {
        dashboardService.deleteDashboard(id);
        return ResponseEntity.noContent().build();
    }

}
