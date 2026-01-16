package com.core.project.job.controller;//package com.itsutra.project.job.controller;
//
//
//import com.itsutra.project.job.dto.JobDashboardStatsDTO;
//import com.itsutra.project.job.dto.JobPositionRequestDTO;
//import com.itsutra.project.job.dto.JobPositionResponseDTO;
//import com.itsutra.project.job.dto.JobPositionUpdateRequestDTO;
//import com.itsutra.project.job.service.JobPositionService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/interviews/job-positions")
//@RequiredArgsConstructor
//public class JobPositionController {
//
//    private final JobPositionService jobPositionService;
//
//    @PostMapping
//    public ResponseEntity<JobPositionResponseDTO> createJobPosition(@Valid @RequestBody JobPositionRequestDTO request) {
//        JobPositionResponseDTO response = jobPositionService.createJobPosition(request);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<JobPositionResponseDTO>> getAllJobPositions() {
//        List<JobPositionResponseDTO> jobPositions = jobPositionService.getAllJobPositions();
//        return ResponseEntity.ok(jobPositions);
//    }
//
//
//
//    @GetMapping("/active")
//    public ResponseEntity<List<JobPositionResponseDTO>> getActiveJobPositions(){
//        List<JobPositionResponseDTO> activePositions = jobPositionService.getActiveJobPositions();
//        return ResponseEntity.ok(activePositions);
//    }
//
//
//
//    @GetMapping("/{id}")
//    public ResponseEntity<JobPositionResponseDTO> getJobPositionById(@PathVariable Long id) {
//        JobPositionResponseDTO response = jobPositionService.getJobPositionById(id);
//        return ResponseEntity.ok(response);
//    }
//
//
//
//    @GetMapping("/code/{code}")
//    public ResponseEntity<JobPositionResponseDTO> getJobPositionByCode(@PathVariable String code) {
//        JobPositionResponseDTO response = jobPositionService.getJobPositionByCode(code);
//        return ResponseEntity.ok(response);
//    }
//
//
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
//
//    @PostMapping("/{id}/publish")
//    public ResponseEntity<JobPositionResponseDTO> publishJobPosition(@PathVariable Long id) {
//        JobPositionResponseDTO response = jobPositionService.publishJobPosition(id);
//        return ResponseEntity.ok(response);
//    }
//
//
//    @PostMapping("/{id}/close")
//    public ResponseEntity<JobPositionResponseDTO> closeJobPosition(@PathVariable Long id) {
//        JobPositionResponseDTO response = jobPositionService.closeJobPosition(id);
//        return ResponseEntity.ok(response);
//    }
//
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
//    public ResponseEntity<List<JobPositionResponseDTO>> getJobPositionsByDepartment(
//            @PathVariable Long departmentId) {
//        List<JobPositionResponseDTO> jobPositions = jobPositionService.getJobPositionsByDepartment(departmentId);
//        return ResponseEntity.ok(jobPositions);
//    }
//
//    @GetMapping("/location/{locationId}")
//    public ResponseEntity<List<JobPositionResponseDTO>> getJobPositionsByLocation(
//            @PathVariable Long locationId) {
//        List<JobPositionResponseDTO> jobPositions = jobPositionService.getJobPositionsByLocation(locationId);
//        return ResponseEntity.ok(jobPositions);
//    }
//
//
//    @GetMapping("/remote")
//    public ResponseEntity<List<JobPositionResponseDTO>> getRemoteJobPositions() {
//
//        List<JobPositionResponseDTO> remotePositions = jobPositionService.getRemoteJobPositions();
//        return ResponseEntity.ok(remotePositions);
//    }
//
//    @GetMapping("/skill/{skillName}")
//    public ResponseEntity<List<JobPositionResponseDTO>> getJobPositionsBySkill(
//            @PathVariable String skillName) {
//
//        List<JobPositionResponseDTO> jobPositions = jobPositionService.getJobPositionsBySkill(skillName);
//        return ResponseEntity.ok(jobPositions);
//    }
//
//    @GetMapping("/dashboard/stats")
//    public ResponseEntity<JobDashboardStatsDTO> getDashboardStats() {
//        JobDashboardStatsDTO stats = jobPositionService.getDashboardStatistics();
//        return ResponseEntity.ok(stats);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteJobPosition(@PathVariable Long id) {
//        jobPositionService.deleteJobPosition(id);
//        return ResponseEntity.noContent().build();
//    }
//
//
//}
