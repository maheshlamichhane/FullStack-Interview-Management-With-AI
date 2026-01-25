package com.core.project.job.service;

import com.core.project.job.dao.DepartmentDAO;
import com.core.project.job.dto.DeleteResponseDTO;
import com.core.project.job.dto.DepartmentRequestDTO;
import com.core.project.job.dto.DepartmentResponseDTO;
import com.core.project.job.entity.Department;
import com.core.project.job.enums.Status;
import com.core.project.job.exception.ApplicationErrors;
import com.core.project.job.mapper.JobMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    private final DepartmentDAO departmentDAO;
    private final JobMapper jobMapper;


    @Transactional
    public Mono<DepartmentResponseDTO> createDepartment(DepartmentRequestDTO request) {

        log.info("Creating new department: {}", request.getName());

          return departmentDAO.existsByName(request.getName())
                  .flatMap(exists -> {
                      if(exists){
                          return ApplicationErrors.departmentAlreadyExistsWithName(request.getName());
                      }
                      if(request.getCode() != null){
                          return departmentDAO.existsByCode(request.getCode())
                                  .flatMap(codeExists -> {
                                      if(codeExists){
                                          return ApplicationErrors.departmentAlreadyExistWithCode(request.getCode());
                                      }
                                      return saveDepartment(request);

                                  });
                      }
                      return saveDepartment(request);
                  });
    }

    private Mono<DepartmentResponseDTO> saveDepartment(DepartmentRequestDTO request) {
        Department department = jobMapper.toDepartmentEntity(request);
        return departmentDAO.save(department)
                .map(saved -> {
                    log.info("Successfully created department with id: {}", saved.getId());
                    return jobMapper.toDepartmentResponse(saved);
                });
    }




    @Transactional(readOnly = true)
    public Flux<DepartmentResponseDTO> getAllDepartments() {
        return departmentDAO.findAll()
                .map(jobMapper::toDepartmentResponse);
    }

    @Transactional(readOnly = true)
    public Flux<DepartmentResponseDTO> getAllNestedDepartments() {
        return departmentDAO.findAll()
                .map(jobMapper::toDepartmentResponse);
    }



    @Transactional
    public Flux<DepartmentResponseDTO> getActiveDepartments() {
        return departmentDAO.findByIsActive(true)
                .map(jobMapper::toDepartmentResponse);
    }




    @Transactional(readOnly = true)
    public Mono<DepartmentResponseDTO> getDepartmentById(Long id) {
        return departmentDAO.findById(id)
                .switchIfEmpty(ApplicationErrors.departmentNotFoundById(id))
                .map(jobMapper::toDepartmentResponse);
    }


    @Transactional
    public Mono<DepartmentResponseDTO> updateDepartment(Long id, DepartmentRequestDTO request) {

        log.info("Updating department with id: {}", id);
        return departmentDAO.findById(id)
                .switchIfEmpty(ApplicationErrors.departmentNotFoundById(id))
                .flatMap(department -> {
                    if(request.getName() != null && !request.getName().equals(department.getName())){
                        return departmentDAO.existsByName(request.getName())
                                .flatMap(exists -> {
                                    if(exists){
                                        return ApplicationErrors.<Department>departmentAlreadyExistsWithName(request.getName());
                                    }
                                    department.setName(request.getName());
                                    return Mono.just(department);
                                });
                    }
                    return Mono.just(department);
                })

                .flatMap(department -> {
                    if(request.getCode() != null && !request.getCode().equals(department.getCode())){
                        return departmentDAO.existsByName(request.getCode())
                                .flatMap(exists -> {
                                    if(exists){
                                        return ApplicationErrors.departmentAlreadyExistWithCode(request.getCode());
                                    }
                                    department.setCode(request.getCode());
                                    return Mono.just(department);
                                });
                    }
                    return Mono.just(department);
                })

                .map(department -> {
                    if(request.getDescription() != null){
                        department.setDescription(request.getDescription());
                    }
                    if(request.getManagerId() != null){
                        department.setManagerId(request.getManagerId());
                    }
                    if(request.getBudgetCode() != null){
                        department.setBudgetCode(request.getBudgetCode());
                    }

                    if(request.getCostCenter() != null){
                        department.setCostCenter(request.getCostCenter());
                    }
                    return department;
                })
                .flatMap(departmentDAO::save)
                .map(jobMapper::toDepartmentResponse);
    }


    @Transactional
    public Mono<DeleteResponseDTO> deleteDepartment(Long id) {
        log.info("Deleting department with id: {}", id);

        return departmentDAO.findById(id)
                .switchIfEmpty(ApplicationErrors.departmentNotFoundById(id))
                .flatMap(department ->
                        departmentDAO.deleteById(department.getId())
                                .then(Mono.just(
                                        DeleteResponseDTO.create(
                                                department.getId(),
                                                Status.SUCCESS
                                        )
                                ))
                );

//        // Check if department has child departments
//        if (!department.getChildDepartments().isEmpty()) {
//            throw new IllegalStateException("Cannot delete department with child departments. Please reassign or delete child departments first.");
//        }
//
//        // Check if department has job positions
//        if (!department.getJobPositions().isEmpty()) {
//            throw new IllegalStateException("Cannot delete department with associated job positions. Please reassign or delete job positions first.");
//        }
//
//        departmentDAO.delete(department);
//        log.info("Successfully deleted department with id: {}", id);
    }
//
//
//    @Transactional(readOnly = true)
//    public List<DepartmentTreeResponseDTO> getDepartmentTree() {
//        log.debug("Fetching department tree");
//        List<Department> rootDepartments = departmentDAO.findRootDepartments();
//        return rootDepartments.stream()
//                .map(jobMapper::toDepartmentTreeResponse)
//                .collect(Collectors.toList());
//    }
//
//
//    @Transactional(readOnly = true)
//    public List<DepartmentResponseDTO> getChildDepartments(Long parentId) {
//        log.debug("Fetching child departments for parent id: {}", parentId);
//
//        if (!departmentDAO.existsById(parentId)) {
//            throw new IllegalArgumentException("Parent department not found with id: " + parentId);
//        }
//
//        return departmentDAO.findActiveChildDepartments(parentId).stream()
//                .map(jobMapper::toDepartmentResponse)
//                .collect(Collectors.toList());
//    }
//
//
//    @Transactional(readOnly = true)
//    public List<DepartmentResponseDTO> getDepartmentsByManager(Long managerId) {
//        log.debug("Fetching departments for manager id: {}", managerId);
//        return departmentDAO.findByManagerId(managerId).stream()
//                .map(jobMapper::toDepartmentResponse)
//                .collect(Collectors.toList());
//    }
//
//
//
//    public DepartmentResponseDTO updateDepartmentStatus(Long id, Boolean isActive) {
//        log.info("Toggling department status for id: {} to {}", id, isActive);
//
//        Department department = departmentDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + id));
//
//        department.setIsActive(isActive);
//        Department updatedDepartment = departmentDAO.save(department);
//
//        log.info("Successfully updated department status for id: {} to {}", id, isActive);
//        return jobMapper.toDepartmentResponse(updatedDepartment);
//    }
//
////    // Helper method to check circular reference
//    private boolean isCircularReference(Department department, Department potentialParent) {
//        Department current = potentialParent;
//        while (current != null) {
//            if (current.getId().equals(department.getId())) {
//                return true;
//            }
//            current = current.getParentDepartment();
//        }
//        return false;
//    }
}
