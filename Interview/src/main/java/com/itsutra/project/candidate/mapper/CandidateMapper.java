//package com.itsutra.project.candidate.mapper;
//
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.itsutra.project.candidate.dto.*;
//import com.itsutra.project.candidate.entity.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class CandidateMapper {
//
//    private final ObjectMapper objectMapper;
//
//    // Candidate Mappings
//    public Candidate toCandidateEntity(CandidateRequestDTO request) {
//        return Candidate.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .phone(request.getPhone())
//                .linkedinUrl(request.getLinkedinUrl())
//                .githubUrl(request.getGithubUrl())
//                .portfolioUrl(request.getPortfolioUrl())
//                .currentCompany(request.getCurrentCompany())
//                .currentPosition(request.getCurrentPosition())
//                .totalExperience(request.getTotalExperience())
//                .currentSalary(request.getCurrentSalary())
//                .expectedSalary(request.getExpectedSalary())
//                .noticePeriod(request.getNoticePeriod())
//                .employmentStatus(request.getEmploymentStatus())
//                .preferredLocation(request.getPreferredLocation())
//                .currentLocation(request.getCurrentLocation())
//                .willingToRelocate(request.getWillingToRelocate())
//                .source(request.getSource())
//                .build();
//    }
//
//    public void toCandidateEntity(CandidateRequestDTO request, Candidate candidate) {
//        candidate.setFirstName(request.getFirstName());
//        candidate.setLastName(request.getLastName());
//        candidate.setPhone(request.getPhone());
//        candidate.setLinkedinUrl(request.getLinkedinUrl());
//        candidate.setGithubUrl(request.getGithubUrl());
//        candidate.setPortfolioUrl(request.getPortfolioUrl());
//        candidate.setCurrentCompany(request.getCurrentCompany());
//        candidate.setCurrentPosition(request.getCurrentPosition());
//        candidate.setTotalExperience(request.getTotalExperience());
//        candidate.setCurrentSalary(request.getCurrentSalary());
//        candidate.setExpectedSalary(request.getExpectedSalary());
//        candidate.setNoticePeriod(request.getNoticePeriod());
//        candidate.setEmploymentStatus(request.getEmploymentStatus());
//        candidate.setPreferredLocation(request.getPreferredLocation());
//        candidate.setCurrentLocation(request.getCurrentLocation());
//        candidate.setWillingToRelocate(request.getWillingToRelocate());
//        candidate.setSource(request.getSource());
//    }
//
//    public CandidateResponseDTO toCandidateResponse(Candidate entity) {
//        CandidateResponseDTO response = new CandidateResponseDTO();
//        response.setId(entity.getId());
//        response.setFirstName(entity.getFirstName());
//        response.setLastName(entity.getLastName());
//        response.setEmail(entity.getEmail());
//        response.setPhone(entity.getPhone());
//        response.setLinkedinUrl(entity.getLinkedinUrl());
//        response.setGithubUrl(entity.getGithubUrl());
//        response.setPortfolioUrl(entity.getPortfolioUrl());
//        response.setCurrentCompany(entity.getCurrentCompany());
//        response.setCurrentPosition(entity.getCurrentPosition());
//        response.setTotalExperience(entity.getTotalExperience());
//        response.setCurrentSalary(entity.getCurrentSalary());
//        response.setExpectedSalary(entity.getExpectedSalary());
//        response.setNoticePeriod(entity.getNoticePeriod());
//        response.setEmploymentStatus(entity.getEmploymentStatus());
//        response.setPreferredLocation(entity.getPreferredLocation());
//        response.setCurrentLocation(entity.getCurrentLocation());
//        response.setWillingToRelocate(entity.getWillingToRelocate());
//        response.setSource(entity.getSource());
//        response.setIsActive(entity.getIsActive());
//        response.setCreatedAt(entity.getCreatedAt());
//        response.setUpdatedAt(entity.getUpdatedAt());
//
//        if (entity.getResumes() != null) {
//            response.setResumes(entity.getResumes().stream()
//                    .map(this::toResumeResponse)
//                    .collect(Collectors.toList()));
//        }
//
//        if (entity.getExperiences() != null) {
//            response.setExperiences(entity.getExperiences().stream()
//                    .map(this::toExperienceResponse)
//                    .collect(Collectors.toList()));
//        }
//
//        if (entity.getEducations() != null) {
//            response.setEducations(entity.getEducations().stream()
//                    .map(this::toEducationResponse)
//                    .collect(Collectors.toList()));
//        }
//
//        if (entity.getSkills() != null) {
//            response.setSkills(entity.getSkills().stream()
//                    .map(this::toCandidateSkillResponse)
//                    .collect(Collectors.toList()));
//        }
//
//        return response;
//    }
//
//    // Resume Mappings
//    public ResumeResponseDTO toResumeResponse(Resume entity) {
//        ResumeResponseDTO response = new ResumeResponseDTO();
//        response.setId(entity.getId());
//        response.setCandidateId(entity.getCandidate().getId());
//        response.setFileName(entity.getFileName());
//        response.setFilePath(entity.getFilePath());
//        response.setFileSize(entity.getFileSize());
//        response.setFileType(entity.getFileType());
//        response.setVersion(entity.getVersion());
//        response.setIsPrimary(entity.getIsPrimary());
//        response.setParsedContent(entity.getParsedContent());
//        response.setParsingStatus(entity.getParsingStatus());
//        response.setParsedSkills(entity.getParsedSkills());
//        response.setParsedExperience(entity.getParsedExperience());
//        response.setUploadedAt(entity.getUploadedAt());
//        return response;
//    }
//
//    // Experience Mappings
//    public Experience toExperienceEntity(ExperienceRequestDTO request, Candidate candidate) {
//        return Experience.builder()
//                .candidate(candidate)
//                .companyName(request.getCompanyName())
//                .position(request.getPosition())
//                .description(request.getDescription())
//                .startDate(request.getStartDate())
//                .endDate(request.getEndDate())
//                .isCurrent(request.getIsCurrent() != null ? request.getIsCurrent() : false)
//                .location(request.getLocation())
//                .employmentType(request.getEmploymentType())
//                .skillsUsed(convertListToJson(request.getSkillsUsed()))
//                .build();
//    }
//
//    public ExperienceResponseDTO toExperienceResponse(Experience entity) {
//        ExperienceResponseDTO response = new ExperienceResponseDTO();
//        response.setId(entity.getId());
//        response.setCandidateId(entity.getCandidate().getId());
//        response.setCompanyName(entity.getCompanyName());
//        response.setPosition(entity.getPosition());
//        response.setDescription(entity.getDescription());
//        response.setStartDate(entity.getStartDate());
//        response.setEndDate(entity.getEndDate());
//        response.setIsCurrent(entity.getIsCurrent());
//        response.setLocation(entity.getLocation());
//        response.setEmploymentType(entity.getEmploymentType());
//        response.setSkillsUsed(convertJsonToList(entity.getSkillsUsed()));
//        return response;
//    }
//
//    // Education Mappings
//    public Education toEducationEntity(EducationRequestDTO request, Candidate candidate) {
//        return Education.builder()
//                .candidate(candidate)
//                .institution(request.getInstitution())
//                .degree(request.getDegree())
//                .fieldOfStudy(request.getFieldOfStudy())
//                .grade(request.getGrade())
//                .startDate(request.getStartDate())
//                .endDate(request.getEndDate())
//                .isCurrent(request.getIsCurrent() != null ? request.getIsCurrent() : false)
//                .description(request.getDescription())
//                .educationLevel(request.getEducationLevel())
//                .build();
//    }
//
//    public EducationResponseDTO toEducationResponse(Education entity) {
//        EducationResponseDTO response = new EducationResponseDTO();
//        response.setId(entity.getId());
//        response.setCandidateId(entity.getCandidate().getId());
//        response.setInstitution(entity.getInstitution());
//        response.setDegree(entity.getDegree());
//        response.setFieldOfStudy(entity.getFieldOfStudy());
//        response.setGrade(entity.getGrade());
//        response.setStartDate(entity.getStartDate());
//        response.setEndDate(entity.getEndDate());
//        response.setIsCurrent(entity.getIsCurrent());
//        response.setDescription(entity.getDescription());
//        response.setEducationLevel(entity.getEducationLevel());
//        return response;
//    }
//
//    // Skill Mappings
//    public CandidateSkill toCandidateSkillEntity(CandidateSkillRequestDTO request, Candidate candidate) {
//        return CandidateSkill.builder()
//                .candidate(candidate)
//                .skillName(request.getSkillName())
//                .proficiencyLevel(request.getProficiencyLevel())
//                .yearsOfExperience(request.getYearsOfExperience())
//                .lastUsed(request.getLastUsed())
//                .isCertified(request.getIsCertified() != null ? request.getIsCertified() : false)
//                .certificationName(request.getCertificationName())
//                .build();
//    }
//
//    public CandidateSkillResponseDTO toCandidateSkillResponse(CandidateSkill entity) {
//        CandidateSkillResponseDTO response = new CandidateSkillResponseDTO();
//        response.setId(entity.getId());
//        response.setCandidateId(entity.getCandidate().getId());
//        response.setSkillName(entity.getSkillName());
//        response.setProficiencyLevel(entity.getProficiencyLevel());
//        response.setYearsOfExperience(entity.getYearsOfExperience());
//        response.setLastUsed(entity.getLastUsed());
//        response.setIsCertified(entity.getIsCertified());
//        response.setCertificationName(entity.getCertificationName());
//        return response;
//    }
//
//    // Helper methods for JSON conversion
//    private String convertListToJson(List<String> list) {
//        try {
//            return list != null ? objectMapper.writeValueAsString(list) : null;
//        } catch (Exception e) {
//            throw new RuntimeException("Error converting list to JSON", e);
//        }
//    }
//
//    private List<String> convertJsonToList(String json) {
//        if (json == null || json.trim().isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        String trimmedJson = json.trim();
//        if ("[]".equals(trimmedJson) || "null".equals(trimmedJson)) {
//            return Collections.emptyList();
//        }
//
//        try {
////            List<String> result = objectMapper.readValue(trimmedJson, new TypeReference<List<String>>() {});
//            return null;
////            return result != null ? result : Collections.emptyList();
//        } catch (Exception e) {
//            return Collections.emptyList();
//        }
//    }
//}
