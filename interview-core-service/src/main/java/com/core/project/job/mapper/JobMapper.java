package com.core.project.job.mapper;//package com.itsutra.project.job.mapper;
//
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.itsutra.project.job.dto.*;
//import com.itsutra.project.job.entity.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class JobMapper {
//
//    private final ObjectMapper objectMapper;
//
//    // JobPosition Mappings
//    public JobPosition toJobPositionEntity(JobPositionRequestDTO request, Department department, Location location) {
//        return JobPosition.builder()
//                .title(request.getTitle())
//                .code(request.getCode())
//                .description(request.getDescription())
//                .responsibilities(request.getResponsibilities())
//                .requirements(request.getRequirements())
//                .benefits(request.getBenefits())
//                .department(department)
//                .location(location)
//                .employmentType(request.getEmploymentType())
//                .experienceLevel(request.getExperienceLevel())
//                .minSalary(request.getMinSalary())
//                .maxSalary(request.getMaxSalary())
//                .salaryCurrency(request.getSalaryCurrency())
//                .openPositions(request.getOpenPositions() != null ? request.getOpenPositions() : 1)
//                .isRemote(request.getIsRemote() != null ? request.getIsRemote() : false)
//                .isHybrid(request.getIsHybrid() != null ? request.getIsHybrid() : false)
//                .applicationDeadline(request.getApplicationDeadline())
//                .build();
//    }
//
//    public JobPositionResponseDTO toJobPositionResponse(JobPosition entity) {
//        JobPositionResponseDTO response = new JobPositionResponseDTO();
//        response.setId(entity.getId());
//        response.setTitle(entity.getTitle());
//        response.setCode(entity.getCode());
//        response.setDescription(entity.getDescription());
//        response.setResponsibilities(entity.getResponsibilities());
//        response.setRequirements(entity.getRequirements());
//        response.setBenefits(entity.getBenefits());
//        response.setDepartment(toDepartmentResponse(entity.getDepartment()));
//        response.setLocation(toLocationResponse(entity.getLocation()));
//        response.setEmploymentType(entity.getEmploymentType());
//        response.setExperienceLevel(entity.getExperienceLevel());
//        response.setMinSalary(entity.getMinSalary());
//        response.setMaxSalary(entity.getMaxSalary());
//        response.setSalaryCurrency(entity.getSalaryCurrency());
//        response.setOpenPositions(entity.getOpenPositions());
//        response.setFilledPositions(entity.getFilledPositions());
//        response.setRemainingPositions(entity.getRemainingPositions());
//        response.setStatus(entity.getStatus());
//        response.setIsRemote(entity.getIsRemote());
//        response.setIsHybrid(entity.getIsHybrid());
//        response.setIsActive(entity.isActive());
//        response.setIsAcceptingApplications(entity.isAcceptingApplications());
//        response.setPublishedAt(entity.getPublishedAt());
//        response.setClosedAt(entity.getClosedAt());
//        response.setApplicationDeadline(entity.getApplicationDeadline());
//        response.setCreatedAt(entity.getCreatedAt());
//        response.setUpdatedAt(entity.getUpdatedAt());
//
//        if (entity.getJobRequirements() != null) {
//            response.setJobRequirements(entity.getJobRequirements().stream()
//                    .map(this::toJobRequirementResponse)
//                    .collect(Collectors.toList()));
//        }
//
//        if (entity.getRequiredSkills() != null) {
//            response.setRequiredSkills(entity.getRequiredSkills().stream()
//                    .map(this::toJobSkillResponse)
//                    .collect(Collectors.toList()));
//        }
//
//        return response;
//    }
//
//    // Department Mappings
//    public Department toDepartmentEntity(DepartmentRequestDTO request) {
//        return Department.builder()
//                .name(request.getName())
//                .code(request.getCode())
//                .description(request.getDescription())
//                .managerId(request.getManagerId())
//                .budgetCode(request.getBudgetCode())
//                .costCenter(request.getCostCenter())
//                .isActive(true)
//                .build();
//    }
//
//    public DepartmentResponseDTO toDepartmentResponse(Department entity) {
//        DepartmentResponseDTO response = new DepartmentResponseDTO();
//        response.setId(entity.getId());
//        response.setName(entity.getName());
//        response.setCode(entity.getCode());
//        response.setDescription(entity.getDescription());
//        response.setManagerId(entity.getManagerId());
//        response.setIsActive(entity.getIsActive());
//        response.setBudgetCode(entity.getBudgetCode());
//        response.setCostCenter(entity.getCostCenter());
//        response.setCreatedAt(entity.getCreatedAt());
//        response.setUpdatedAt(entity.getUpdatedAt());
//
//        if (entity.getJobPositions() != null) {
//            response.setJobPositionCount(entity.getJobPositions().size());
//        }
//
//        return response;
//    }
//
//    public DepartmentTreeResponseDTO toDepartmentTreeResponse(Department entity) {
//        DepartmentTreeResponseDTO response = new DepartmentTreeResponseDTO();
//        response.setId(entity.getId());
//        response.setName(entity.getName());
//        response.setCode(entity.getCode());
//
//        if (entity.getChildDepartments() != null) {
//            response.setChildren(entity.getChildDepartments().stream()
//                    .map(this::toDepartmentTreeResponse)
//                    .collect(Collectors.toList()));
//        }
//
//        if (entity.getJobPositions() != null) {
//            response.setTotalPositions(entity.getJobPositions().size());
//            response.setOpenPositions((int) entity.getJobPositions().stream()
//                    .filter(JobPosition::isAcceptingApplications)
//                    .count());
//        }
//
//        return response;
//    }
//
//    // Location Mappings
//    public Location toLocationEntity(LocationRequestDTO request) {
//        return Location.builder()
//                .name(request.getName())
//                .code(request.getCode())
//                .address(request.getAddress())
//                .city(request.getCity())
//                .state(request.getState())
//                .country(request.getCountry())
//                .postalCode(request.getPostalCode())
//                .timezone(request.getTimezone())
//                .contactPerson(request.getContactPerson())
//                .contactEmail(request.getContactEmail())
//                .contactPhone(request.getContactPhone())
//                .facilities(convertListToJson(request.getFacilities()))
//                .isActive(true)
//                .isRemote(request.isRemote()  ? request.isRemote() : false)
//                .build();
//    }
//
//    public LocationResponseDTO toLocationResponse(Location entity) {
//        LocationResponseDTO response = new LocationResponseDTO();
//        response.setId(entity.getId());
//        response.setName(entity.getName());
//        response.setCode(entity.getCode());
//        response.setAddress(entity.getAddress());
//        response.setCity(entity.getCity());
//        response.setState(entity.getState());
//        response.setCountry(entity.getCountry());
//        response.setPostalCode(entity.getPostalCode());
//        response.setTimezone(entity.getTimezone());
//        response.setFullAddress(entity.getFullAddress());
//        response.setIsActive(entity.getIsActive());
//        response.setIsRemote(entity.getIsRemote());
//        response.setContactPerson(entity.getContactPerson());
//        response.setContactEmail(entity.getContactEmail());
//        response.setContactPhone(entity.getContactPhone());
//        response.setFacilities(convertJsonToList(entity.getFacilities()));
//        response.setCreatedAt(entity.getCreatedAt());
//        response.setUpdatedAt(entity.getUpdatedAt());
//
//        if (entity.getJobPositions() != null) {
//            response.setJobPositionCount(entity.getJobPositions().size());
//        }
//
//        return response;
//    }
//
//    // Requirement & Skill Mappings
//    public JobRequirement toJobRequirementEntity(JobRequirementRequestDTO request, JobPosition jobPosition) {
//        return JobRequirement.builder()
//                .jobPosition(jobPosition)
//                .requirementType(request.getRequirementType())
//                .description(request.getDescription())
//                .isMandatory(request.getIsMandatory() != null ? request.getIsMandatory() : true)
//                .priority(request.getPriority() != null ? request.getPriority() : 1)
//                .build();
//    }
//
//    public JobRequirementResponseDTO toJobRequirementResponse(JobRequirement entity) {
//        JobRequirementResponseDTO response = new JobRequirementResponseDTO();
//        response.setId(entity.getId());
//        response.setJobPositionId(entity.getJobPosition().getId());
//        response.setRequirementType(entity.getRequirementType());
//        response.setDescription(entity.getDescription());
//        response.setIsMandatory(entity.getIsMandatory());
//        response.setPriority(entity.getPriority());
//        return response;
//    }
//
//    public JobSkill toJobSkillEntity(JobSkillRequestDTO request, JobPosition jobPosition) {
//        return JobSkill.builder()
//                .jobPosition(jobPosition)
//                .skillName(request.getSkillName())
//                .category(request.getCategory())
//                .proficiencyLevel(request.getProficiencyLevel())
//                .isMandatory(request.getIsMandatory() != null ? request.getIsMandatory() : true)
//                .minExperienceYears(request.getMinExperienceYears())
//                .priority(request.getPriority() != null ? request.getPriority() : 1)
//                .build();
//    }
//
//    public JobSkillResponseDTO toJobSkillResponse(JobSkill entity) {
//        JobSkillResponseDTO response = new JobSkillResponseDTO();
//        response.setId(entity.getId());
//        response.setJobPositionId(entity.getJobPosition().getId());
//        response.setSkillName(entity.getSkillName());
//        response.setCategory(entity.getCategory());
//        response.setProficiencyLevel(entity.getProficiencyLevel());
//        response.setIsMandatory(entity.getIsMandatory());
//        response.setMinExperienceYears(entity.getMinExperienceYears());
//        response.setPriority(entity.getPriority());
//        return response;
//    }
//
//    // Helper methods for JSON conversion
//    public String convertListToJson(List<String> list) {
//        try {
//            return list != null ? objectMapper.writeValueAsString(list) : null;
//        } catch (Exception e) {
//            throw new RuntimeException("Error converting list to JSON", e);
//        }
//    }
//
//    private List<String> convertJsonToList(String json) {
//        try {
////            return json != null ? objectMapper.readValue(json, new TypeReference<List<String>>() {}) : null;
//            return null;
//        } catch (Exception e) {
//            throw new RuntimeException("Error converting JSON to list", e);
//        }
//    }
//}
