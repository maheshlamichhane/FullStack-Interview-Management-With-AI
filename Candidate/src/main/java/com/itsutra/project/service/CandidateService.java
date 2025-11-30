package com.itsutra.project.service;

import com.itsutra.project.dao.CandidateDAO;
import com.itsutra.project.dto.CandidateRequestDTO;
import com.itsutra.project.dto.CandidateResponseDTO;
import com.itsutra.project.dto.CandidateSearchRequestDTO;
import com.itsutra.project.entity.Candidate;
import com.itsutra.project.exception.ResourceNotFoundException;
import com.itsutra.project.mapper.CandidateMapper;
import com.itsutra.project.utilities.CandidateSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateDAO candidateDAO;
    private final CandidateMapper candidateMapper;

    @Transactional
    public CandidateResponseDTO createCandidate(CandidateRequestDTO request) {
        log.info("Creating new candidate: {}", request.getEmail());

        // Check if candidate already exists
        if (candidateDAO.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Candidate with email " + request.getEmail() + " already exists");
        }

        Candidate candidate = candidateMapper.toCandidateEntity(request);
        Candidate savedCandidate = candidateDAO.save(candidate);

        log.info("Created candidate with ID: {}", savedCandidate.getId());
        return candidateMapper.toCandidateResponse(savedCandidate);
    }

    public CandidateResponseDTO getCandidateById(Long id) throws ResourceNotFoundException {
        Candidate candidate = candidateDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
        return candidateMapper.toCandidateResponse(candidate);
    }

    public CandidateResponseDTO getCandidateByEmail(String email) throws ResourceNotFoundException {
        Candidate candidate = candidateDAO.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with email: " + email));
        return candidateMapper.toCandidateResponse(candidate);
    }

    public Page<CandidateResponseDTO> getAllCandidates(Pageable pageable) {
        Page<Candidate> candidates = candidateDAO.findAll(pageable);
        return candidates.map(candidateMapper::toCandidateResponse);
    }

    public Page<CandidateResponseDTO> searchCandidates(CandidateSearchRequestDTO searchRequest, Pageable pageable) {
        Specification<Candidate> spec = CandidateSpecification.withSearchCriteria(searchRequest);
        Page<Candidate> candidates = candidateDAO.findAll(spec, pageable);
        return candidates.map(candidateMapper::toCandidateResponse);
    }

    @Transactional
    public CandidateResponseDTO updateCandidate(Long id, CandidateRequestDTO request) throws ResourceNotFoundException {
        Candidate candidate = candidateDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));

        // Update fields
        candidate.setFirstName(request.getFirstName());
        candidate.setLastName(request.getLastName());
        candidate.setPhone(request.getPhone());
        candidate.setLinkedinUrl(request.getLinkedinUrl());
        candidate.setGithubUrl(request.getGithubUrl());
        candidate.setPortfolioUrl(request.getPortfolioUrl());
        candidate.setCurrentCompany(request.getCurrentCompany());
        candidate.setCurrentPosition(request.getCurrentPosition());
        candidate.setTotalExperience(request.getTotalExperience());
        candidate.setCurrentSalary(request.getCurrentSalary());
        candidate.setExpectedSalary(request.getExpectedSalary());
        candidate.setNoticePeriod(request.getNoticePeriod());
        candidate.setEmploymentStatus(request.getEmploymentStatus());
        candidate.setPreferredLocation(request.getPreferredLocation());
        candidate.setCurrentLocation(request.getCurrentLocation());
        candidate.setWillingToRelocate(request.getWillingToRelocate());
        candidate.setSource(request.getSource());

        Candidate updatedCandidate = candidateDAO.save(candidate);
        log.info("Updated candidate with ID: {}", id);

        return candidateMapper.toCandidateResponse(updatedCandidate);
    }

    @Transactional
    public void deleteCandidate(Long id) throws ResourceNotFoundException {
        Candidate candidate = candidateDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));

        candidateDAO.delete(candidate);
        log.info("Deleted candidate with ID: {}", id);
    }

    @Transactional
    public CandidateResponseDTO deactivateCandidate(Long id) throws ResourceNotFoundException {
        Candidate candidate = candidateDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));

        candidate.setIsActive(false);
        Candidate updatedCandidate = candidateDAO.save(candidate);
        log.info("Deactivated candidate with ID: {}", id);

        return candidateMapper.toCandidateResponse(updatedCandidate);
    }

    @Transactional
    public CandidateResponseDTO activateCandidate(Long id) throws ResourceNotFoundException {
        Candidate candidate = candidateDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));

        candidate.setIsActive(true);
        Candidate updatedCandidate = candidateDAO.save(candidate);
        log.info("Activated candidate with ID: {}", id);

        return candidateMapper.toCandidateResponse(updatedCandidate);
    }

    public List<String> getCandidateSkills(Long candidateId) throws ResourceNotFoundException {
        Candidate candidate = candidateDAO.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));

        return candidate.getSkills().stream()
                .map(skill -> skill.getSkillName())
                .collect(Collectors.toList());
    }

    public Long getCandidateCount() {
        return candidateDAO.count();
    }

    public Long getActiveCandidateCount() {
        return candidateDAO.findAll().stream()
                .filter(Candidate::getIsActive)
                .count();
    }
}
