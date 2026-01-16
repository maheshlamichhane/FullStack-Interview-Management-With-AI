package com.core.project.job.service;//package com.itsutra.project.job.service;
//
//import com.itsutra.project.job.dao.*;
//import com.itsutra.project.job.dto.*;
//import com.itsutra.project.job.entity.*;
//import com.itsutra.project.job.enums.JobStatus;
//import com.itsutra.project.job.mapper.JobMapper;
//import jakarta.persistence.criteria.Predicate;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//@Slf4j
//public class JobPositionService {
//
//    private final JobPositionDAO jobPositionDAO;
//    private final DepartmentDAO departmentDAO;
//    private final LocationDAO locationDAO;
//    private final JobRequirementDAO jobRequirementDAO;
//
//    private final JobSkillDAO jobSkillDAO;
//    private final JobMapper jobMapper;
//
//    @Transactional
//    public JobPositionResponseDTO createJobPosition(JobPositionRequestDTO request) {
//        log.info("Creating new job position with code: {}", request.getCode());
//
//        // Validate unique code
//        if (jobPositionDAO.existsByCode(request.getCode())) {
//            throw new IllegalArgumentException("Job position code already exists: " + request.getCode());
//        }
//
//        // Validate department and location
//        Department department = departmentDAO.findById(request.getDepartmentId())
//                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + request.getDepartmentId()));
//
//        Location location = locationDAO.findById(request.getLocationId())
//                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + request.getLocationId()));
//
//        // Create job position
//        JobPosition jobPosition = jobMapper.toJobPositionEntity(request, department, location);
//        JobPosition savedJobPosition = jobPositionDAO.save(jobPosition);
//
//        // Save requirements and skills
//        saveJobRequirements(request.getJobRequirements(), savedJobPosition);
//        saveJobSkills(request.getRequiredSkills(), savedJobPosition);
//
//        log.info("Successfully created job position with id: {}", savedJobPosition.getId());
//        return jobMapper.toJobPositionResponse(savedJobPosition);
//    }
//
//    @Transactional(readOnly = true)
//    public List<JobPositionResponseDTO> getAllJobPositions() {
//        log.debug("Fetching all job positions with filters");
//        List<JobPosition> allJobs =   jobPositionDAO.findAll();
//        return allJobs.stream().map(jobMapper::toJobPositionResponse).collect(Collectors.toList());
//    }
//
//
//    @Transactional(readOnly = true)
//    public List<JobPositionResponseDTO> getActiveJobPositions() {
//        log.debug("Fetching active job positions");
//        List<JobPosition> activePositions = jobPositionDAO.findActivePositions(LocalDateTime.now());
//        return activePositions.stream().map(jobMapper::toJobPositionResponse).collect(Collectors.toList());
//    }
//
//
//
//    @Transactional(readOnly = true)
//    public JobPositionResponseDTO getJobPositionById(Long id) {
//        log.debug("Fetching job position by id: {}", id);
//        JobPosition jobPosition = jobPositionDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Job position not found with id: " + id));
//        return jobMapper.toJobPositionResponse(jobPosition);
//    }
//
//
//
//    @Transactional(readOnly = true)
//    public JobPositionResponseDTO getJobPositionByCode(String code) {
//        log.debug("Fetching job position by code: {}", code);
//        JobPosition jobPosition = jobPositionDAO.findByCode(code)
//                .orElseThrow(() -> new IllegalArgumentException("Job position not found with code: " + code));
//        return jobMapper.toJobPositionResponse(jobPosition);
//    }
//
//
//    @Transactional
//    public JobPositionResponseDTO updateJobPosition(Long id, JobPositionUpdateRequestDTO request) {
//        log.info("Updating job position with id: {}", id);
//
//        JobPosition jobPosition = jobPositionDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Job position not found with id: " + id));
//
//        // Update fields if provided
//        Optional.ofNullable(request.getTitle()).ifPresent(jobPosition::setTitle);
//        Optional.ofNullable(request.getDescription()).ifPresent(jobPosition::setDescription);
//        Optional.ofNullable(request.getResponsibilities()).ifPresent(jobPosition::setResponsibilities);
//        Optional.ofNullable(request.getRequirements()).ifPresent(jobPosition::setRequirements);
//        Optional.ofNullable(request.getBenefits()).ifPresent(jobPosition::setBenefits);
//        Optional.ofNullable(request.getEmploymentType()).ifPresent(jobPosition::setEmploymentType);
//        Optional.ofNullable(request.getExperienceLevel()).ifPresent(jobPosition::setExperienceLevel);
//        Optional.ofNullable(request.getMinSalary()).ifPresent(jobPosition::setMinSalary);
//        Optional.ofNullable(request.getMaxSalary()).ifPresent(jobPosition::setMaxSalary);
//        Optional.ofNullable(request.getSalaryCurrency()).ifPresent(jobPosition::setSalaryCurrency);
//        Optional.ofNullable(request.getOpenPositions()).ifPresent(jobPosition::setOpenPositions);
//        Optional.ofNullable(request.getIsRemote()).ifPresent(jobPosition::setIsRemote);
//        Optional.ofNullable(request.getIsHybrid()).ifPresent(jobPosition::setIsHybrid);
//        Optional.ofNullable(request.getApplicationDeadline()).ifPresent(jobPosition::setApplicationDeadline);
//
//        // Update department if provided
//        if (request.getDepartmentId() != null) {
//            Department department = departmentDAO.findById(request.getDepartmentId())
//                    .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + request.getDepartmentId()));
//            jobPosition.setDepartment(department);
//        }
//
//        // Update location if provided
//        if (request.getLocationId() != null) {
//            Location location = locationDAO.findById(request.getLocationId())
//                    .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + request.getLocationId()));
//            jobPosition.setLocation(location);
//        }
//
//
//        JobPosition updatedJobPosition = jobPositionDAO.save(jobPosition);
//        log.info("Successfully updated job position with id: {}", id);
//        return jobMapper.toJobPositionResponse(updatedJobPosition);
//    }
//
//
//    @Transactional
//    public JobPositionResponseDTO publishJobPosition(Long id) {
//        log.info("Publishing job position with id: {}", id);
//
//        JobPosition jobPosition = jobPositionDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Job position not found with id: " + id));
//
//        if (jobPosition.getStatus() == JobStatus.PUBLISHED) {
//            throw new IllegalStateException("Job position is already published");
//        }
//
//        jobPosition.setStatus(JobStatus.PUBLISHED);
//        jobPosition.setPublishedAt(LocalDateTime.now());
//
//        JobPosition publishedJobPosition = jobPositionDAO.save(jobPosition);
//        log.info("Successfully published job position with id: {}", id);
//        return jobMapper.toJobPositionResponse(publishedJobPosition);
//    }
//
//    @Transactional
//    public JobPositionResponseDTO closeJobPosition(Long id) {
//        log.info("Closing job position with id: {}", id);
//
//        JobPosition jobPosition = jobPositionDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Job position not found with id: " + id));
//
//        jobPosition.setStatus(JobStatus.CLOSED);
//        jobPosition.setClosedAt(LocalDateTime.now());
//
//        JobPosition closedJobPosition = jobPositionDAO.save(jobPosition);
//        log.info("Successfully closed job position with id: {}", id);
//        return jobMapper.toJobPositionResponse(closedJobPosition);
//    }
//
//
//    @Transactional
//    public JobPositionResponseDTO updateFilledPositions(Long id, Integer filledCount) {
//        log.info("Updating filled positions for job position with id: {} to {}", id, filledCount);
//
//        JobPosition jobPosition = jobPositionDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Job position not found with id: " + id));
//
//        if (filledCount > jobPosition.getOpenPositions()) {
//            throw new IllegalArgumentException("Filled positions cannot exceed open positions");
//        }
//
//        jobPosition.setFilledPositions(filledCount);
//
//        JobPosition updatedJobPosition = jobPositionDAO.save(jobPosition);
//        log.info("Successfully updated filled positions for job position with id: {}", id);
//        return jobMapper.toJobPositionResponse(updatedJobPosition);
//    }
//
//
//    @Transactional(readOnly = true)
//    public List<JobPositionResponseDTO> getJobPositionsByDepartment(Long departmentId) {
//        log.debug("Fetching job positions for department id: {}", departmentId);
//
//        if (!departmentDAO.existsById(departmentId)) {
//            throw new IllegalArgumentException("Department not found with id: " + departmentId);
//        }
//
//        List<JobPosition> jobPositions = jobPositionDAO.findByDepartmentId(departmentId);
//        return jobPositions.stream().map(jobMapper::toJobPositionResponse).collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<JobPositionResponseDTO> getJobPositionsByLocation(Long locationId) {
//        log.debug("Fetching job positions for location id: {}", locationId);
//
//        if (!locationDAO.existsById(locationId)) {
//            throw new IllegalArgumentException("Location not found with id: " + locationId);
//        }
//
//        List<JobPosition> jobPositions = jobPositionDAO.findByLocationId(locationId);
//        return jobPositions.stream().map(jobMapper::toJobPositionResponse).collect(Collectors.toList());
//    }
//
//
//    @Transactional(readOnly = true)
//    public List<JobPositionResponseDTO> getRemoteJobPositions() {
//        log.debug("Fetching remote job positions");
//        List<JobPosition> remotePositions = jobPositionDAO.findRemotePositions();
//        return remotePositions.stream().map(jobMapper::toJobPositionResponse).collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<JobPositionResponseDTO> getJobPositionsBySkill(String skillName) {
//        log.debug("Fetching job positions requiring skill: {}", skillName);
//        List<JobPosition> jobPositions = jobPositionDAO.findByRequiredSkill(skillName);
//        return jobPositions.stream().map(jobMapper::toJobPositionResponse).collect(Collectors.toList());
//    }
//
//
//    @Transactional(readOnly = true)
//    public JobDashboardStatsDTO getDashboardStatistics() {
//        log.debug("Fetching dashboard statistics");
//
//        Long totalPositions = jobPositionDAO.count();
//        Long activePositions = jobPositionDAO.countActivePositions();
//        List<JobPosition> expiredPositions = jobPositionDAO.findExpiredPositions(LocalDateTime.now());
//
//        return JobDashboardStatsDTO.builder()
//                .totalPositions(totalPositions)
//                .activePositions(activePositions)
//                .expiredPositions((long) expiredPositions.size())
//                .draftPositions(jobPositionDAO.findByStatus(JobStatus.DRAFT, Pageable.unpaged()).getTotalElements())
//                .closedPositions(jobPositionDAO.findByStatus(JobStatus.CLOSED, Pageable.unpaged()).getTotalElements())
//                .build();
//    }
//
//
//    @Transactional
//    public void deleteJobPosition(Long id) {
//        log.info("Deleting job position with id: {}", id);
//        if (!jobPositionDAO.existsById(id)) {
//            throw new IllegalArgumentException("Job position not found with id: " + id);
//        }
//        jobPositionDAO.deleteById(id);
//        log.info("Successfully deleted job position with id: {}", id);
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////
//
////
////    // Get Job Positions by Location
//
////
//
////
////    // Get Job Positions by Skill
//
////
//
//    // Helper method to build search specification
//    private Specification<JobPosition> buildSearchSpecification(JobPositionSearchRequestDTO searchRequest) {
//        return (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            if (searchRequest.getTitle() != null) {
//                predicates.add(criteriaBuilder.like(
//                        criteriaBuilder.lower(root.get("title")),
//                        "%" + searchRequest.getTitle().toLowerCase() + "%"
//                ));
//            }
//
//            if (searchRequest.getCode() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("code"), searchRequest.getCode()));
//            }
//
//            if (searchRequest.getDepartmentId() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("department").get("id"), searchRequest.getDepartmentId()));
//            }
//
//            if (searchRequest.getLocationId() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("location").get("id"), searchRequest.getLocationId()));
//            }
//
//            if (searchRequest.getEmploymentType() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("employmentType"), searchRequest.getEmploymentType()));
//            }
//
//            if (searchRequest.getExperienceLevel() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("experienceLevel"), searchRequest.getExperienceLevel()));
//            }
//
//            if (searchRequest.getMinSalary() != null) {
//                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("minSalary"), searchRequest.getMinSalary()));
//            }
//
//            if (searchRequest.getMaxSalary() != null) {
//                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxSalary"), searchRequest.getMaxSalary()));
//            }
//
//            if (searchRequest.getIsRemote() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("isRemote"), searchRequest.getIsRemote()));
//            }
//
//            if (searchRequest.getStatus() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("status"),JobStatus.valueOf(searchRequest.getStatus())));
//            }
//
//            if (searchRequest.getIsActive() != null && searchRequest.getIsActive()) {
//                predicates.add(criteriaBuilder.equal(root.get("status"), JobStatus.PUBLISHED));
//                predicates.add(criteriaBuilder.or(
//                        criteriaBuilder.isNull(root.get("applicationDeadline")),
//                        criteriaBuilder.greaterThan(root.get("applicationDeadline"), LocalDateTime.now())
//                ));
//            }
//
//            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//        };
//    }
////
//    // Helper methods for saving requirements and skills
//    private void saveJobRequirements(List<JobRequirementRequestDTO> requirements, JobPosition jobPosition) {
//        if (requirements != null && !requirements.isEmpty()) {
//            List<JobRequirement> jobRequirements = requirements.stream()
//                    .map(req -> jobMapper.toJobRequirementEntity(req, jobPosition))
//                    .collect(Collectors.toList());
//            jobRequirementDAO.saveAll(jobRequirements);
//        }
//    }
//
//    private void saveJobSkills(List<JobSkillRequestDTO> skills, JobPosition jobPosition) {
//        if (skills != null && !skills.isEmpty()) {
//            List<JobSkill> jobSkills = skills.stream()
//                    .map(skill -> jobMapper.toJobSkillEntity(skill, jobPosition))
//                    .collect(Collectors.toList());
//            jobSkillDAO.saveAll(jobSkills);
//        }
//    }
//
//}
