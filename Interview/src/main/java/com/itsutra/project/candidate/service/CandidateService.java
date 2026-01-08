//package com.itsutra.project.candidate.service;
//
//
//import com.itsutra.project.candidate.dao.CandidateDAO;
//import com.itsutra.project.candidate.dto.CandidateRequestDTO;
//import com.itsutra.project.candidate.dto.CandidateResponseDTO;
//import com.itsutra.project.candidate.dto.CandidateSearchRequestDTO;
//import com.itsutra.project.candidate.entity.Candidate;
//import com.itsutra.project.candidate.exception.ResourceNotFoundException;
//import com.itsutra.project.candidate.mapper.CandidateMapper;
//import com.itsutra.project.candidate.utilities.CandidateSpecification;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class CandidateService {
//
//    private final CandidateDAO candidateDAO;
//    private final CandidateMapper candidateMapper;
//
//    @Transactional
//    public CandidateResponseDTO createCandidate(CandidateRequestDTO request) {
//
//        log.info("Creating new candidate: {}", request.getEmail());
//
//        // Check if candidate already exists
//        if (candidateDAO.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("Candidate with email " + request.getEmail() + " already exists");
//        }
//
//        Candidate candidate = candidateMapper.toCandidateEntity(request);
//        Candidate savedCandidate = candidateDAO.save(candidate);
//
//        log.info("Created candidate with ID: {}", savedCandidate.getId());
//        return candidateMapper.toCandidateResponse(savedCandidate);
//    }
//
//    @Transactional
//    public CandidateResponseDTO getCandidateById(Long id) throws ResourceNotFoundException {
//        Candidate candidate = candidateDAO.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
//        return candidateMapper.toCandidateResponse(candidate);
//    }
//
//
//
//    @Transactional
//    public CandidateResponseDTO getCandidateByEmail(String email) throws ResourceNotFoundException {
//        Candidate candidate = candidateDAO.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with email: " + email));
//        return candidateMapper.toCandidateResponse(candidate);
//    }
//
//
//
//    @Transactional
//    public List<CandidateResponseDTO> getAllCandidates() {
//        List<Candidate> candidates = candidateDAO.findAll();
//        return candidates.stream().map(candidateMapper::toCandidateResponse).collect(Collectors.toList());
//    }
//
//
//    @Transactional
//    public List<CandidateResponseDTO> searchCandidates(CandidateSearchRequestDTO searchRequest) {
//        Specification<Candidate> spec = CandidateSpecification.withSearchCriteria(searchRequest);
//        List<Candidate> candidates = candidateDAO.findAll(spec);
//        return candidates.stream().map(candidateMapper::toCandidateResponse).collect(Collectors.toList());
//    }
//
//
//
//    @Transactional
//    public CandidateResponseDTO updateCandidate(Long id, CandidateRequestDTO request) throws ResourceNotFoundException {
//        Candidate candidate = candidateDAO.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
//        candidateMapper.toCandidateEntity(request,candidate);
//
//        Candidate updatedCandidate = candidateDAO.save(candidate);
//        log.info("Updated candidate with ID: {}", id);
//
//        return candidateMapper.toCandidateResponse(updatedCandidate);
//    }
//
//
//
//    @Transactional
//    public void deleteCandidate(Long id) throws ResourceNotFoundException {
//        Candidate candidate = candidateDAO.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
//
//        candidateDAO.delete(candidate);
//        log.info("Deleted candidate with ID: {}", id);
//    }
//
//    @Transactional
//    public CandidateResponseDTO deactivateCandidate(Long id) throws ResourceNotFoundException {
//        Candidate candidate = candidateDAO.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
//
//        candidate.setIsActive(false);
//        Candidate updatedCandidate = candidateDAO.save(candidate);
//        log.info("Deactivated candidate with ID: {}", id);
//
//        return candidateMapper.toCandidateResponse(updatedCandidate);
//    }
//
//
//
//    @Transactional
//    public CandidateResponseDTO activateCandidate(Long id) throws ResourceNotFoundException {
//        Candidate candidate = candidateDAO.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
//
//        candidate.setIsActive(true);
//        Candidate updatedCandidate = candidateDAO.save(candidate);
//        log.info("Activated candidate with ID: {}", id);
//
//        return candidateMapper.toCandidateResponse(updatedCandidate);
//    }
//
//
//
//
//    @Transactional
//    public List<String> getCandidateSkills(Long candidateId) throws ResourceNotFoundException {
//        Candidate candidate = candidateDAO.findById(candidateId)
//                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));
//
//        return candidate.getSkills().stream()
//                .map(skill -> skill.getSkillName())
//                .collect(Collectors.toList());
//    }
//
//
//
//    @Transactional
//    public Long getCandidateCount() {
//        return candidateDAO.count();
//    }
//
//
//    @Transactional
//    public Long getActiveCandidateCount() {
//        return candidateDAO.findAll().stream()
//                .filter(Candidate::getIsActive)
//                .count();
//    }
//}
