package com.itsutra.project.controller;


import com.itsutra.project.dto.*;
import com.itsutra.project.service.JobPositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/job-positions")
@RequiredArgsConstructor
public class JobPositionController {

    private final JobPositionService jobPositionService;

    @PostMapping
    public ResponseEntity<JobPositionResponseDTO> createJobPosition(@Valid @RequestBody JobPositionRequestDTO request) {
        JobPositionResponseDTO response = jobPositionService.createJobPosition(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//    @GetMapping
//    public ResponseEntity<Page<JobPositionResponseDTO>> getAllJobPositions(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "desc") String sortDirection,
//            @ModelAttribute JobPositionSearchRequestDTO searchRequest) {
//
//        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        Page<JobPositionResponseDTO> jobPositions = jobPositionService.getAllJobPositions(searchRequest, pageable);
//        return ResponseEntity.ok(jobPositions);
//    }
//
//    @GetMapping("/active")
//    public ResponseEntity<Page<JobPositionResponseDTO>> getActiveJobPositions(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<JobPositionResponseDTO> activePositions = jobPositionService.getActiveJobPositions(pageable);
//        return ResponseEntity.ok(activePositions);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<JobPositionResponseDTO> getJobPositionById(@PathVariable Long id) {
//        JobPositionResponseDTO response = jobPositionService.getJobPositionById(id);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/code/{code}")
//    public ResponseEntity<JobPositionResponseDTO> getJobPositionByCode(@PathVariable String code) {
//        JobPositionResponseDTO response = jobPositionService.getJobPositionByCode(code);
//        return ResponseEntity.ok(response);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<JobPositionResponseDTO> updateJobPosition(
//            @PathVariable Long id,
//            @Valid @RequestBody JobPositionUpdateRequestDTO request) {
//
//        JobPositionResponseDTO response = jobPositionService.updateJobPosition(id, request);
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteJobPosition(@PathVariable Long id) {
//        jobPositionService.deleteJobPosition(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/{id}/publish")
//    public ResponseEntity<JobPositionResponseDTO> publishJobPosition(@PathVariable Long id) {
//        JobPositionResponseDTO response = jobPositionService.publishJobPosition(id);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/{id}/close")
//    public ResponseEntity<JobPositionResponseDTO> closeJobPosition(@PathVariable Long id) {
//        JobPositionResponseDTO response = jobPositionService.closeJobPosition(id);
//        return ResponseEntity.ok(response);
//    }
//
//    @PatchMapping("/{id}/filled-positions")
//    public ResponseEntity<JobPositionResponseDTO> updateFilledPositions(
//            @PathVariable Long id,
//            @RequestBody Map<String, Integer> request) {
//
//        Integer filledCount = request.get("filledCount");
//        JobPositionResponseDTO response = jobPositionService.updateFilledPositions(id, filledCount);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/department/{departmentId}")
//    public ResponseEntity<Page<JobPositionResponseDTO>> getJobPositionsByDepartment(
//            @PathVariable Long departmentId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<JobPositionResponseDTO> jobPositions = jobPositionService.getJobPositionsByDepartment(departmentId, pageable);
//        return ResponseEntity.ok(jobPositions);
//    }
//
//    @GetMapping("/location/{locationId}")
//    public ResponseEntity<Page<JobPositionResponseDTO>> getJobPositionsByLocation(
//            @PathVariable Long locationId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<JobPositionResponseDTO> jobPositions = jobPositionService.getJobPositionsByLocation(locationId, pageable);
//        return ResponseEntity.ok(jobPositions);
//    }
//
//    @GetMapping("/remote")
//    public ResponseEntity<Page<JobPositionResponseDTO>> getRemoteJobPositions(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<JobPositionResponseDTO> remotePositions = jobPositionService.getRemoteJobPositions(pageable);
//        return ResponseEntity.ok(remotePositions);
//    }
//
//    @GetMapping("/skill/{skillName}")
//    public ResponseEntity<Page<JobPositionResponseDTO>> getJobPositionsBySkill(
//            @PathVariable String skillName,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<JobPositionResponseDTO> jobPositions = jobPositionService.getJobPositionsBySkill(skillName, pageable);
//        return ResponseEntity.ok(jobPositions);
//    }
//
//    @GetMapping("/dashboard/stats")
//    public ResponseEntity<JobDashboardStatsDTO> getDashboardStats() {
//        JobDashboardStatsDTO stats = jobPositionService.getDashboardStatistics();
//        return ResponseEntity.ok(stats);
//    }
}
