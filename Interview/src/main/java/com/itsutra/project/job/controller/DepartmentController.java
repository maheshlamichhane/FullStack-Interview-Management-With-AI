package com.itsutra.project.job.controller;

import com.itsutra.project.job.dto.DepartmentRequestDTO;
import com.itsutra.project.job.dto.DepartmentResponseDTO;
import com.itsutra.project.job.dto.DepartmentTreeResponseDTO;
import com.itsutra.project.job.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interviews/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentRequestDTO request) {
        DepartmentResponseDTO response = departmentService.createDepartment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DepartmentResponseDTO>> getActiveDepartments() {
        List<DepartmentResponseDTO> departments = departmentService.getActiveDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id) {
        DepartmentResponseDTO response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequestDTO request) {

        DepartmentResponseDTO response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/tree")
    public ResponseEntity<List<DepartmentTreeResponseDTO>> getDepartmentTree() {

        List<DepartmentTreeResponseDTO> tree = departmentService.getDepartmentTree();
        return ResponseEntity.ok(tree);
    }



    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<DepartmentResponseDTO>> getChildDepartments(@PathVariable Long parentId) {
        List<DepartmentResponseDTO> children = departmentService.getChildDepartments(parentId);
        return ResponseEntity.ok(children);
    }



    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<DepartmentResponseDTO>> getDepartmentsByManager(@PathVariable Long managerId) {
        List<DepartmentResponseDTO> departments = departmentService.getDepartmentsByManager(managerId);
        return ResponseEntity.ok(departments);
    }



    @PatchMapping("/{id}/status")
    public ResponseEntity<DepartmentResponseDTO> toggleDepartmentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> request) {

        Boolean isActive = request.get("isActive");
        DepartmentResponseDTO response = departmentService.updateDepartmentStatus(id, isActive);
        return ResponseEntity.ok(response);
    }
}