package com.itsutra.project.service;

import com.itsutra.project.dao.DepartmentDAO;
import com.itsutra.project.dto.DepartmentRequestDTO;
import com.itsutra.project.dto.DepartmentResponseDTO;
import com.itsutra.project.dto.DepartmentTreeResponseDTO;
import com.itsutra.project.entity.Department;
import com.itsutra.project.mapper.JobMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    private final DepartmentDAO departmentDAO;
    private final JobMapper jobMapper;

    @Transactional
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO request) {
        log.info("Creating new department: {}", request.getName());

        // Validate unique name and code
        if (departmentDAO.existsByName(request.getName())) {
            throw new IllegalArgumentException("Department name already exists: " + request.getName());
        }
        if (request.getCode() != null && departmentDAO.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Department code already exists: " + request.getCode());
        }

        Department department = jobMapper.toDepartmentEntity(request);

        // Set parent department if provided
        if (request.getParentDepartmentId() != null) {
            Department parentDepartment = departmentDAO.findById(request.getParentDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent department not found with id: " + request.getParentDepartmentId()));
            department.setParentDepartment(parentDepartment);
        }

        Department savedDepartment = departmentDAO.save(department);
        log.info("Successfully created department with id: {}", savedDepartment.getId());
        return jobMapper.toDepartmentResponse(savedDepartment);
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getAllDepartments() {
        log.debug("Fetching all departments");
        return departmentDAO.findAll().stream()
                .map(jobMapper::toDepartmentResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<DepartmentResponseDTO> getActiveDepartments() {
        log.debug("Fetching active departments");
        return departmentDAO.findByIsActive(true).stream()
                .map(jobMapper::toDepartmentResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentById(Long id) {
        log.debug("Fetching department by id: {}", id);
        Department department = departmentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + id));
        return jobMapper.toDepartmentResponse(department);
    }


    @Transactional
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request) {

        log.info("Updating department with id: {}", id);

        Department department = departmentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + id));

        // Check for duplicate name
        if (request.getName() != null && !request.getName().equals(department.getName())) {
            if (departmentDAO.existsByName(request.getName())) {
                throw new IllegalArgumentException("Department name already exists: " + request.getName());
            }
            department.setName(request.getName());
        }

        // Check for duplicate code
        if (request.getCode() != null && !request.getCode().equals(department.getCode())) {
            if (departmentDAO.existsByCode(request.getCode())) {
                throw new IllegalArgumentException("Department code already exists: " + request.getCode());
            }
            department.setCode(request.getCode());
        }

        // Update other fields
        Optional.ofNullable(request.getDescription()).ifPresent(department::setDescription);
        Optional.ofNullable(request.getManagerId()).ifPresent(department::setManagerId);
        Optional.ofNullable(request.getBudgetCode()).ifPresent(department::setBudgetCode);
        Optional.ofNullable(request.getCostCenter()).ifPresent(department::setCostCenter);

        // Update parent department if provided
        if (request.getParentDepartmentId() != null) {
            Department parentDepartment = departmentDAO.findById(request.getParentDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent department not found with id: " + request.getParentDepartmentId()));

            // Prevent circular reference
            if (isCircularReference(department, parentDepartment)) {
                throw new IllegalArgumentException("Circular reference detected in department hierarchy");
            }

            department.setParentDepartment(parentDepartment);
        } else if (request.getParentDepartmentId() == null && department.getParentDepartment() != null) {
            department.setParentDepartment(null);
        }

        Department updatedDepartment = departmentDAO.save(department);
        log.info("Successfully updated department with id: {}", id);
        return jobMapper.toDepartmentResponse(updatedDepartment);
    }


    @Transactional
    public void deleteDepartment(Long id) {
        log.info("Deleting department with id: {}", id);

        Department department = departmentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + id));

        // Check if department has child departments
        if (!department.getChildDepartments().isEmpty()) {
            throw new IllegalStateException("Cannot delete department with child departments. Please reassign or delete child departments first.");
        }

        // Check if department has job positions
        if (!department.getJobPositions().isEmpty()) {
            throw new IllegalStateException("Cannot delete department with associated job positions. Please reassign or delete job positions first.");
        }

        departmentDAO.delete(department);
        log.info("Successfully deleted department with id: {}", id);
    }


    @Transactional(readOnly = true)
    public List<DepartmentTreeResponseDTO> getDepartmentTree() {
        log.debug("Fetching department tree");
        List<Department> rootDepartments = departmentDAO.findRootDepartments();
        return rootDepartments.stream()
                .map(jobMapper::toDepartmentTreeResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getChildDepartments(Long parentId) {
        log.debug("Fetching child departments for parent id: {}", parentId);

        if (!departmentDAO.existsById(parentId)) {
            throw new IllegalArgumentException("Parent department not found with id: " + parentId);
        }

        return departmentDAO.findActiveChildDepartments(parentId).stream()
                .map(jobMapper::toDepartmentResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getDepartmentsByManager(Long managerId) {
        log.debug("Fetching departments for manager id: {}", managerId);
        return departmentDAO.findByManagerId(managerId).stream()
                .map(jobMapper::toDepartmentResponse)
                .collect(Collectors.toList());
    }



    public DepartmentResponseDTO updateDepartmentStatus(Long id, Boolean isActive) {
        log.info("Toggling department status for id: {} to {}", id, isActive);

        Department department = departmentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + id));

        department.setIsActive(isActive);
        Department updatedDepartment = departmentDAO.save(department);

        log.info("Successfully updated department status for id: {} to {}", id, isActive);
        return jobMapper.toDepartmentResponse(updatedDepartment);
    }

//    // Helper method to check circular reference
    private boolean isCircularReference(Department department, Department potentialParent) {
        Department current = potentialParent;
        while (current != null) {
            if (current.getId().equals(department.getId())) {
                return true;
            }
            current = current.getParentDepartment();
        }
        return false;
    }
}
