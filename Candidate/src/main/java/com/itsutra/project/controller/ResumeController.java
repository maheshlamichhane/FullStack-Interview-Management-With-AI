package com.itsutra.project.controller;


import com.itsutra.project.dto.ResumeRequestDTO;
import com.itsutra.project.dto.ResumeResponseDTO;
import com.itsutra.project.exception.ResourceNotFoundException;
import com.itsutra.project.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<ResumeResponseDTO> uploadResume(@ModelAttribute ResumeRequestDTO request) {
        ResumeResponseDTO response = resumeService.uploadResume(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<ResumeResponseDTO>> getCandidateResumes(@PathVariable Long candidateId) {
        List<ResumeResponseDTO> responses = resumeService.getCandidateResumes(candidateId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/candidate/{candidateId}/primary")
    public ResponseEntity<ResumeResponseDTO> getPrimaryResume(@PathVariable Long candidateId) throws ResourceNotFoundException {
        ResumeResponseDTO response = resumeService.getPrimaryResume(candidateId);
        return ResponseEntity.ok(response);
    }



    @PatchMapping("/{resumeId}/primary")
    public ResponseEntity<ResumeResponseDTO> setPrimaryResume(@PathVariable Long resumeId) throws ResourceNotFoundException {
        ResumeResponseDTO response = resumeService.setPrimaryResume(resumeId);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long resumeId) throws ResourceNotFoundException {
        resumeService.deleteResume(resumeId);
        return ResponseEntity.noContent().build();
    }

}