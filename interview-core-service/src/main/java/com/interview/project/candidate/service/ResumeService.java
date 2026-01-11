package com.interview.project.candidate.service;//package com.itsutra.project.candidate.service;
//
//
//
//import com.itsutra.project.candidate.dao.CandidateDAO;
//import com.itsutra.project.candidate.dao.ResumeDAO;
//import com.itsutra.project.candidate.dto.ResumeRequestDTO;
//import com.itsutra.project.candidate.dto.ResumeResponseDTO;
//import com.itsutra.project.candidate.entity.Candidate;
//import com.itsutra.project.candidate.entity.Resume;
//import com.itsutra.project.candidate.exception.ResourceNotFoundException;
//import com.itsutra.project.candidate.mapper.CandidateMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class ResumeService {
//
//    private final ResumeDAO resumeDAO;
//    private final CandidateDAO candidateDAO;
//    private final CandidateMapper candidateMapper;
//    private final ResumeParserService resumeParserService;
//
//    private final Path fileStorageLocation = Paths.get("uploads/resumes").toAbsolutePath().normalize();
//
//
//    @Transactional
//    public ResumeResponseDTO uploadResume(ResumeRequestDTO request) {
//        try {
//            Candidate candidate = candidateDAO.findById(request.getCandidateId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + request.getCandidateId()));
//
//            MultipartFile file = request.getFile();
//            String fileName = generateFileName(candidate, file.getOriginalFilename());
//
//            // Create upload directory if it doesn't exist
//            Files.createDirectories(fileStorageLocation);
//
//            // Save file to disk
//            Path targetLocation = fileStorageLocation.resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation);
//
//            // Create resume entity
//            Resume resume = Resume.builder()
//                    .candidate(candidate)
//                    .fileName(fileName)
//                    .filePath(targetLocation.toString())
//                    .fileSize(file.getSize())
//                    .fileType(file.getContentType())
//                    .isPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false)
//                    .build();
//
//            // If this is primary, unset other primary resumes
//            if (resume.getIsPrimary()) {
//                unsetOtherPrimaryResumes(candidate.getId());
//            }
//
//            Resume savedResume = resumeDAO.save(resume);
//
//            // Start async resume parsing
//            resumeParserService.parseResumeAsync(savedResume);
//
//            log.info("Uploaded resume for candidate {}: {}", candidate.getId(), fileName);
//            return candidateMapper.toResumeResponse(savedResume);
//
//        } catch (IOException ex) {
//            log.error("Failed to upload resume", ex);
//            throw new RuntimeException("Failed to upload resume: " + ex.getMessage());
//        } catch (ResourceNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//
//    @Transactional
//    public List<ResumeResponseDTO> getCandidateResumes(Long candidateId) {
//        List<Resume> resumes = resumeDAO.findByCandidateId(candidateId);
//        return resumes.stream()
//                .map(candidateMapper::toResumeResponse)
//                .collect(Collectors.toList());
//    }
//
//
//
//
//
//    public ResumeResponseDTO getPrimaryResume(Long candidateId) throws ResourceNotFoundException {
//        Resume resume = resumeDAO.findByCandidateIdAndIsPrimary(candidateId, true)
//                .orElseThrow(() -> new ResourceNotFoundException("Primary resume not found for candidate: " + candidateId));
//        return candidateMapper.toResumeResponse(resume);
//    }
//
//    @Transactional
//    public ResumeResponseDTO setPrimaryResume(Long resumeId) throws ResourceNotFoundException {
//        Resume resume = resumeDAO.findById(resumeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with id: " + resumeId));
//
//        // Unset other primary resumes
//        unsetOtherPrimaryResumes(resume.getCandidate().getId());
//
//        // Set this as primary
//        resume.setIsPrimary(true);
//        Resume updatedResume = resumeDAO.save(resume);
//
//        log.info("Set resume {} as primary for candidate {}", resumeId, resume.getCandidate().getId());
//        return candidateMapper.toResumeResponse(updatedResume);
//    }
//
//    @Transactional
//    public void deleteResume(Long resumeId) throws ResourceNotFoundException {
//        Resume resume = resumeDAO.findById(resumeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with id: " + resumeId));
//
//        try {
//            // Delete file from disk
//            Files.deleteIfExists(Paths.get(resume.getFilePath()));
//
//            // Delete from database
//            resumeDAO.delete(resume);
//
//            log.info("Deleted resume with ID: {}", resumeId);
//        } catch (IOException ex) {
//            log.error("Failed to delete resume file", ex);
//            throw new RuntimeException("Failed to delete resume file");
//        }
//    }
//
//    // Helper methods
//    private String generateFileName(Candidate candidate, String originalFileName) {
//        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
//        return String.format("candidate_%d_%d%s",
//                candidate.getId(),
//                System.currentTimeMillis(),
//                fileExtension);
//    }
//
//    private void unsetOtherPrimaryResumes(Long candidateId) {
//        List<Resume> primaryResumes = resumeDAO.findByCandidateId(candidateId).stream()
//                .filter(Resume::getIsPrimary)
//                .collect(Collectors.toList());
//
//        for (Resume resume : primaryResumes) {
//            resume.setIsPrimary(false);
//        }
//        resumeDAO.saveAll(primaryResumes);
//    }
//}
