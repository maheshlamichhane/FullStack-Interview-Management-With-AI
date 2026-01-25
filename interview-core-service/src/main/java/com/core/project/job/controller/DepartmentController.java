package com.core.project.job.controller;

import com.core.project.job.dto.DeleteResponseDTO;
import com.core.project.job.dto.DepartmentRequestDTO;
import com.core.project.job.dto.DepartmentResponseDTO;
import com.core.project.job.dto.JobDTO;
import com.core.project.job.service.DepartmentService;
import com.core.project.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interviews/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    private final JobService jobService;

    @MutationMapping
    public Mono<DepartmentResponseDTO> createDepartment(@Argument("department")  DepartmentRequestDTO department) {
        return departmentService.createDepartment(department);
    }


    @SchemaMapping(typeName = "Query")
    public Flux<DepartmentResponseDTO> getAllDepartments() {
        System.out.println("Inside all departments");
        return  departmentService.getAllDepartments();
    }

    @BatchMapping(typeName = "DepartmentNestedResponse")
    public Mono<Map<DepartmentResponseDTO, List<JobDTO>>> jobs(List<DepartmentResponseDTO> departments) {
        System.out.println("Inside jobs fetching info "+departments);
        List<Long> deptIds = departments.stream()
                .map(DepartmentResponseDTO::getId)
                .collect(Collectors.toList());

        return jobService.findAllJobByDepartmentId(deptIds)
                .collectList()
                .map(jobs -> {
                    Map<Long, List<JobDTO>> jobsByDeptId = jobs.stream()
                            .map(job -> new JobDTO(job.getId(), job.getName(), job.getDepartmentId()))
                            .collect(Collectors.groupingBy(JobDTO::getDepartmentId));

                    Map<DepartmentResponseDTO, List<JobDTO>> result = new HashMap<>();
                    for (DepartmentResponseDTO d : departments) {
                        result.put(d, jobsByDeptId.getOrDefault(d.getId(), Collections.emptyList()));
                    }
                    return result;
                });
    }


    @QueryMapping
    public Flux<DepartmentResponseDTO> getAllNestedDepartments() {
        return  departmentService.getAllNestedDepartments();
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