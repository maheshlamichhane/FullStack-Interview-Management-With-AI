package com.itsutra.project.controller;

import com.itsutra.project.dto.*;
import com.itsutra.project.service.ReportExportMediaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.itsutra.project.service.ReportService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ReportExportMediaterService reportExportMediaterService;

    @PostMapping
    public ResponseEntity<ReportResponseDTO> createReport(@Valid @RequestBody ReportRequestDTO request) {
        ReportResponseDTO response = reportService.createReport(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> getReportById(@PathVariable Long id) {
        ReportResponseDTO response = reportService.getReportById(id);
        return ResponseEntity.ok(response);
    }



    @GetMapping
    public ResponseEntity<List<ReportResponseDTO>> getAllReports() {
        List<ReportResponseDTO> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }


    @GetMapping("/code/{code}")
    public ResponseEntity<ReportResponseDTO> getReportByCode(@PathVariable String code) {
        ReportResponseDTO report = reportService.getAllReportsByCode(code);
        return ResponseEntity.ok(report);
    }



    @PutMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> updateReport(
            @PathVariable Long id,
            @Valid @RequestBody ReportRequestDTO request) {

        ReportResponseDTO response = reportService.updateReport(id, request);
        return ResponseEntity.ok(response);
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
            @RequestBody Map<String, Object> parameters) {

        ReportDataResponseDTO response = reportExportMediaterService.getReportData(id, parameters);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }



//    @GetMapping("/{id}/analytics")
//    public ResponseEntity<Map<String, Object>> getReportAnalytics(@PathVariable Long id) {
//        Map<String, Object> analytics = reportService.getReportAnalytics(id);
//        return ResponseEntity.ok(analytics);
//    }
//
//    @GetMapping("/category/{category}")
//    public ResponseEntity<Page<ReportResponseDTO>> getReportsByCategory(
//
//            @PathVariable String category,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        // Implementation would filter by category
//        return ResponseEntity.ok(Page.empty());
//    }



}
