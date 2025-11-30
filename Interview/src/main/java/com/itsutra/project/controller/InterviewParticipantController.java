package com.itsutra.project.controller;


import com.itsutra.project.dto.ParticipantRequestDTO;
import com.itsutra.project.dto.ParticipantResponseDTO;
import com.itsutra.project.dto.ParticipantUpdateDTO;
import com.itsutra.project.enums.ParticipantRole;
import com.itsutra.project.service.InterviewParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class InterviewParticipantController {

    private final InterviewParticipantService participantService;

    @PostMapping
    public ResponseEntity<ParticipantResponseDTO> addParticipant(@Valid @RequestBody ParticipantRequestDTO requestDTO) {
        ParticipantResponseDTO response = participantService.addParticipant(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/interview/{interviewId}")
    public ResponseEntity<List<ParticipantResponseDTO>> getParticipantsByInterview(
            @PathVariable Long interviewId) {
        List<ParticipantResponseDTO> participants = participantService.getParticipantsByInterview(interviewId);
        return ResponseEntity.ok(participants);
    }


    @GetMapping("/participant/{participantId}")
    public ResponseEntity<ParticipantResponseDTO> getParticipantsByParticipantId(
            @PathVariable Long participantId) {
        ParticipantResponseDTO participant = participantService.getParticipantsByParticipantId(participantId);
        return ResponseEntity.ok(participant);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ParticipantResponseDTO> getParticipantById(@PathVariable Long id) {
        ParticipantResponseDTO participant = participantService.getParticipantById(id);
        return ResponseEntity.ok(participant);
    }



    @PutMapping
    public ResponseEntity<ParticipantResponseDTO> updateParticipant(
            @Valid @RequestBody ParticipantUpdateDTO updateDTO) {
        ParticipantResponseDTO updatedParticipant = participantService.updateParticipant(updateDTO.getId(), updateDTO);
        return ResponseEntity.ok(updatedParticipant);
    }



    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmAttendance(@PathVariable Long id) {
        participantService.confirmAttendance(id);
        return ResponseEntity.ok().build();
    }




    @PatchMapping("/{id}/attend")
    public ResponseEntity<Void> markAsAttended(@PathVariable Long id) {
        participantService.markAsAttended(id);
        return ResponseEntity.ok().build();
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long id) {
        participantService.removeParticipant(id);
        return ResponseEntity.noContent().build();
    }



    @DeleteMapping("/interview/{interviewId}/participant/{participantId}")
    public ResponseEntity<Void> removeParticipantFromInterview(
            @PathVariable Long interviewId,
            @PathVariable Long participantId) {
        participantService.removeParticipantFromInterview(interviewId, participantId);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/interview/{interviewId}/role/{role}")
    public ResponseEntity<List<ParticipantResponseDTO>> getParticipantsByInterviewAndRole(
            @PathVariable Long interviewId,
            @PathVariable ParticipantRole role) {
        List<ParticipantResponseDTO> participants = participantService.getParticipantsByInterviewAndRole(interviewId, role);
        return ResponseEntity.ok(participants);
    }




    @GetMapping("/interview/{interviewId}/confirmed-count")
    public ResponseEntity<Long> getConfirmedParticipantsCount(@PathVariable Long interviewId) {
        Long count = participantService.getConfirmedParticipantsCount(interviewId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/interview/{interviewId}/participant/{participantId}/confirmed")
    public ResponseEntity<Boolean> isParticipantConfirmed(
            @PathVariable Long interviewId,
            @PathVariable Long participantId) {
        boolean isConfirmed = participantService.isParticipantConfirmed(interviewId, participantId);
        return ResponseEntity.ok(isConfirmed);
    }
}