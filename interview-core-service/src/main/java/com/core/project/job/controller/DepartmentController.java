package com.core.project.job.controller;

import com.core.project.job.dto.DeleteResponseDTO;
import com.core.project.job.dto.DepartmentRequestDTO;
import com.core.project.job.dto.DepartmentResponseDTO;
import com.core.project.job.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/interviews/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @MutationMapping
    public Mono<DepartmentResponseDTO> createDepartment(@Argument("department")  DepartmentRequestDTO department) {
        return departmentService.createDepartment(department);
    }


    @QueryMapping
    public Flux<DepartmentResponseDTO> getAllDepartments() {
        return  departmentService.getAllDepartments();
    }


    @QueryMapping
    public Flux<DepartmentResponseDTO> getActiveDepartments() {
        return departmentService.getActiveDepartments();
    }


    @QueryMapping
    public Mono<DepartmentResponseDTO> getDepartmentById(@Argument("id") Long id) {
       return departmentService.getDepartmentById(id);
    }

    @MutationMapping
    public Mono<DepartmentResponseDTO> updateDepartment(
            @Argument("id") Long id,
            @Argument("department") DepartmentRequestDTO department) {
        return departmentService.updateDepartment(id, department);
    }


    @MutationMapping
    public Mono<DeleteResponseDTO> deleteDepartment(@Argument("id") Long id) {
        return departmentService.deleteDepartment(id);
    }



//
//
//    @GetMapping("/tree")
//    public ResponseEntity<List<DepartmentTreeResponseDTO>> getDepartmentTree() {
//
//        List<DepartmentTreeResponseDTO> tree = departmentService.getDepartmentTree();
//        return ResponseEntity.ok(tree);
//    }
//
//
//
//    @GetMapping("/{parentId}/children")
//    public ResponseEntity<List<DepartmentResponseDTO>> getChildDepartments(@PathVariable Long parentId) {
//        List<DepartmentResponseDTO> children = departmentService.getChildDepartments(parentId);
//        return ResponseEntity.ok(children);
//    }
//
//
//
//    @GetMapping("/manager/{managerId}")
//    public ResponseEntity<List<DepartmentResponseDTO>> getDepartmentsByManager(@PathVariable Long managerId) {
//        List<DepartmentResponseDTO> departments = departmentService.getDepartmentsByManager(managerId);
//        return ResponseEntity.ok(departments);
//    }
//
//
//
//    @PatchMapping("/{id}/status")
//    public ResponseEntity<DepartmentResponseDTO> toggleDepartmentStatus(
//            @PathVariable Long id,
//            @RequestBody Map<String, Boolean> request) {
//
//        Boolean isActive = request.get("isActive");
//        DepartmentResponseDTO response = departmentService.updateDepartmentStatus(id, isActive);
//        return ResponseEntity.ok(response);
//    }
}