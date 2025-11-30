package com.itsutra.project.controller;

import com.itsutra.project.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.itsutra.project.service.ReportService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponseDTO> createReport(@Valid @RequestBody ReportRequestDTO request) {
        ReportResponseDTO response = reportService.createReport(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ReportResponseDTO>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ReportResponseDTO> reports = reportService.getAllReports(pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> getReportById(@PathVariable Long id) {
        ReportResponseDTO response = reportService.getReportById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ReportResponseDTO> getReportByCode(@PathVariable String code) {
        // Implementation would find by code
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> updateReport(
            @PathVariable Long id,
            @Valid @RequestBody ReportRequestDTO request) {

        ReportResponseDTO response = reportService.updateReport(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/execute")
    public ResponseEntity<ReportExecutionResponseDTO> executeReport(
            @PathVariable Long id,
            @RequestBody ReportExecutionRequestDTO request) {

        ReportExecutionResponseDTO response = reportService.executeReport(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/data")
    public ResponseEntity<ReportDataResponseDTO> getReportData(
            @PathVariable Long id,
            @RequestParam Map<String, Object> parameters) {

        ReportDataResponseDTO response = reportService.getReportData(id, parameters);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<Map<String, Object>> getReportAnalytics(@PathVariable Long id) {
        Map<String, Object> analytics = reportService.getReportAnalytics(id);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ReportResponseDTO>> getReportsByCategory(

            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        // Implementation would filter by category
        return ResponseEntity.ok(Page.empty());
    }
}
