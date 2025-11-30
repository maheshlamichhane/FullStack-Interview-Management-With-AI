package com.itsutra.project.controller;

import com.itsutra.project.dto.CandidateRequestDTO;
import com.itsutra.project.dto.CandidateResponseDTO;
import com.itsutra.project.dto.CandidateSearchRequestDTO;
import com.itsutra.project.exception.ResourceNotFoundException;
import com.itsutra.project.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<CandidateResponseDTO> createCandidate(@Valid @RequestBody CandidateRequestDTO request) {
        CandidateResponseDTO response = candidateService.createCandidate(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponseDTO> getCandidate(@PathVariable Long id) throws ResourceNotFoundException {
        CandidateResponseDTO response = candidateService.getCandidateById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CandidateResponseDTO> getCandidateByEmail(@PathVariable String email) throws ResourceNotFoundException {
        CandidateResponseDTO response = candidateService.getCandidateByEmail(email);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<CandidateResponseDTO>> getAllCandidates() {
        List<CandidateResponseDTO> responses = candidateService.getAllCandidates();
        return ResponseEntity.ok(responses);
    }



    @PostMapping("/search")
    public ResponseEntity<List<CandidateResponseDTO>> searchCandidates(
            @RequestBody CandidateSearchRequestDTO searchRequest) {
        List<CandidateResponseDTO> responses = candidateService.searchCandidates(searchRequest);
        return ResponseEntity.ok(responses);
    }



    @PutMapping("/{id}")
    public ResponseEntity<CandidateResponseDTO> updateCandidate(
            @PathVariable Long id,
            @Valid @RequestBody CandidateRequestDTO request) throws ResourceNotFoundException {
        CandidateResponseDTO response = candidateService.updateCandidate(id, request);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) throws ResourceNotFoundException {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CandidateResponseDTO> deactivateCandidate(@PathVariable Long id) throws ResourceNotFoundException {
        CandidateResponseDTO response = candidateService.deactivateCandidate(id);
        return ResponseEntity.ok(response);
    }



    @PatchMapping("/{id}/activate")
    public ResponseEntity<CandidateResponseDTO> activateCandidate(@PathVariable Long id) throws ResourceNotFoundException {
        CandidateResponseDTO response = candidateService.activateCandidate(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/skills")
    public ResponseEntity<List<String>> getCandidateSkills(@PathVariable Long id) throws ResourceNotFoundException {
        List<String> skills = candidateService.getCandidateSkills(id);
        return ResponseEntity.ok(skills);
    }


    @GetMapping("/count")
    public ResponseEntity<Long> getCandidateCount() {
        Long count = candidateService.getCandidateCount();
        return ResponseEntity.ok(count);
    }



    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveCandidateCount() {
        Long count = candidateService.getActiveCandidateCount();
        return ResponseEntity.ok(count);
    }

}
