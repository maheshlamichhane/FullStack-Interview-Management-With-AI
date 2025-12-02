package com.itsutra.project.controller;

import com.itsutra.project.dto.*;
import com.itsutra.project.service.MetricService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricService metricService;

    @PostMapping
    public ResponseEntity<MetricResponseDTO> createMetric(@Valid @RequestBody MetricRequestDTO request) {
        MetricResponseDTO response = metricService.createMetric(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetricResponseDTO> getMetricById(@PathVariable Long id) {
        MetricResponseDTO response = metricService.getMetricById(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<MetricResponseDTO>> getAllMetrics() {
        List<MetricResponseDTO> metrics = metricService.getAllMetrics();
        return ResponseEntity.ok(metrics);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetric(@PathVariable Long id) {
        metricService.deleteMetric(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<MetricResponseDTO> updateMetric(
            @PathVariable Long id,
            @Valid @RequestBody MetricRequestDTO request) {

        MetricResponseDTO response = metricService.updateMetric(id, request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/values")
    public ResponseEntity<MetricValueResponseDTO> addMetricValue(@Valid @RequestBody MetricValueRequestDTO request) {
        MetricValueResponseDTO response = metricService.addMetricValue(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




    @GetMapping("/{id}/values")
    public ResponseEntity<List<MetricValueResponseDTO>> getMetricValues(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<MetricValueResponseDTO> values = metricService.getMetricValues(id, startDate, endDate);
        return ResponseEntity.ok(values);
    }



    @GetMapping("/{id}/trend")
    public ResponseEntity<MetricTrendResponseDTO> getMetricTrend(
            @PathVariable Long id,
            @RequestParam(defaultValue = "30D") String timeRange) {

        MetricTrendResponseDTO response = metricService.getMetricTrend(id, timeRange);
        return ResponseEntity.ok(response);
    }


//
//    @PostMapping("/{id}/calculate")
//    public ResponseEntity<Void> calculateMetric(@PathVariable Long id) {
//        // Implementation would trigger metric calculation
//        return ResponseEntity.accepted().build();
//    }
//
//    @GetMapping("/overview")
//    public ResponseEntity<Map<String, Object>> getMetricsOverview() {
//        Map<String, Object> overview = metricService.getMetricsOverview();
//        return ResponseEntity.ok(overview);
//    }
//
//    @GetMapping("/category/{category}")
//    public ResponseEntity<Page<MetricResponseDTO>> getMetricsByCategory(
//            @PathVariable String category,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        // Implementation would filter by category
//        return ResponseEntity.ok(Page.empty());
//    }
}
