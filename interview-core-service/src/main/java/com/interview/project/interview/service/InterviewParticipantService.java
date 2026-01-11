package com.interview.project.interview.service;//package com.itsutra.project.interview.service;
//
//
//import com.itsutra.project.interview.dao.InterviewParticipantRepository;
//import com.itsutra.project.interview.dao.InterviewRepository;
//import com.itsutra.project.interview.dto.ParticipantRequestDTO;
//import com.itsutra.project.interview.dto.ParticipantResponseDTO;
//import com.itsutra.project.interview.dto.ParticipantUpdateDTO;
//import com.itsutra.project.interview.entity.Interview;
//import com.itsutra.project.interview.entity.InterviewParticipant;
//import com.itsutra.project.interview.enums.ParticipantRole;
//import com.itsutra.project.interview.exception.ResourceNotFoundException;
//import com.itsutra.project.interview.mapper.InterviewParticipantMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class InterviewParticipantService {
//
//    private final InterviewParticipantRepository participantRepository;
//    private final InterviewRepository interviewRepository;
//    private final InterviewParticipantMapper participantMapper;
//    private final Long interviewerId = 567284L;
//
//    @Transactional
//    public ParticipantResponseDTO addParticipant(ParticipantRequestDTO requestDTO) {
//        // Check if interview exists and belongs to the interviewer
//        Interview interview = interviewRepository.findByIdAndInterviewerId(requestDTO.getInterviewId(), interviewerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + interviewerId));
//
//        // Check if participant already exists for this interview
//        if (participantRepository.existsByParticipantIdAndInterviewIdAndInterviewerId(
//                requestDTO.getParticipantId(),requestDTO.getInterviewId(),interviewerId)){
//            throw new IllegalArgumentException("Participant already exists for this interview");
//        }
//
//        // Use mapper to convert DTO to Entity
//        InterviewParticipant participant = participantMapper.toEntity(requestDTO, interview,interviewerId);
//
//        InterviewParticipant savedParticipant = participantRepository.save(participant);
//        log.info("Added participant {} to interview {}", requestDTO.getParticipantId(), interviewerId);
//
//        // Use mapper to convert Entity to Response DTO
//        return participantMapper.toResponseDTO(savedParticipant);
//    }
//
//    @Transactional
//    public List<ParticipantResponseDTO> getParticipantsByInterview(Long interviewId) {
//
//        List<InterviewParticipant> participants = participantRepository.findByInterviewIdAndInterviewerId(interviewId,interviewerId);
//        return participants.stream()
//                .map(participantMapper::toResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//
//
//
//    @Transactional
//    public ParticipantResponseDTO getParticipantsByParticipantId(Long participantId) {
//        InterviewParticipant participant = participantRepository.findByParticipantIdAndInterviewerId(participantId,interviewerId)
//                .orElseThrow(() -> new ResourceNotFoundException("participant not found with participant id: " + participantId));
//        return participantMapper.toResponseDTO(participant);
//    }
//
//
//
//    @Transactional
//    public ParticipantResponseDTO getParticipantById(Long id) {
//        InterviewParticipant participant = participantRepository.findByIdAndInterviewerId(id,interviewerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
//        return participantMapper.toResponseDTO(participant);
//    }
//
//
//
//
//    @Transactional
//    public ParticipantResponseDTO updateParticipant(Long id, ParticipantUpdateDTO updateDTO) {
//        InterviewParticipant participant = participantRepository.findByIdAndInterviewerId(id,interviewerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
//
//        // Use mapper to update entity from DTO
//        participantMapper.updateEntityFromUpdateDTO(updateDTO, participant);
//
//        InterviewParticipant updatedParticipant = participantRepository.save(participant);
//        log.info("Updated participant with id: {}", id);
//
//        return participantMapper.toResponseDTO(updatedParticipant);
//    }
//
//
//
//    @Transactional
//    public void confirmAttendance(Long id) {
//        InterviewParticipant participant = participantRepository.findByIdAndInterviewerId(id,interviewerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
//
//        participant.setConfirmedAttendance(true);
//        participantRepository.save(participant);
//        log.info("Confirmed attendance for participant with id: {}", id);
//    }
//
//
//
//    @Transactional
//    public void markAsAttended(Long id) {
//        InterviewParticipant participant = participantRepository.findByIdAndInterviewerId(id,interviewerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
//
//        participant.setAttended(true);
//        participantRepository.save(participant);
//        log.info("Marked participant with id: {} as attended", id);
//    }
//
//
//
//    @Transactional
//    public void removeParticipant(Long id) {
//       participantRepository.findByIdAndInterviewerId(id,interviewerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
//        participantRepository.deleteById(id);
//        log.info("Removed participant with id: {}", id);
//    }
//
//
//
//
//
//    @Transactional
//    public void removeParticipantFromInterview(Long interviewId, Long participantId) {
//        // Verify the interview belongs to the interviewer
//        if (!interviewRepository.existsByIdAndInterviewerId(interviewId, interviewerId)) {
//            throw new ResourceNotFoundException("Interview not found with id: " + interviewId);
//        }
//
//        if (!participantRepository.existsByParticipantIdAndInterviewIdAndInterviewerId(participantId,interviewId,interviewerId)) {
//            throw new ResourceNotFoundException("Participant not found in this interview");
//        }
//
//        participantRepository.deleteByInterviewIdAndParticipantIdAndInterviewerId(interviewId, participantId,interviewerId);
//        log.info("Removed participant {} from interview {}", participantId, interviewId);
//    }
//
//
//
//
//    @Transactional
//    public List<ParticipantResponseDTO> getParticipantsByInterviewAndRole(Long interviewId, ParticipantRole role) {
//
//        // Verify the interview belongs to the interviewer
//        if (!interviewRepository.existsByIdAndInterviewerId(interviewId, interviewerId)) {
//            throw new ResourceNotFoundException("Interview not found with id: " + interviewId);
//        }
//
//        List<InterviewParticipant> participants = participantRepository.findByInterviewIdAndRole(interviewId, role);
//        return participants.stream()
//                .map(participantMapper::toResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//
//
//    public Long getConfirmedParticipantsCount(Long interviewId) {
//        // Verify the interview belongs to the interviewer
//        if (!interviewRepository.existsByIdAndInterviewerId(interviewId, interviewerId)) {
//            throw new ResourceNotFoundException("Interview not found with id: " + interviewId);
//        }
//        return participantRepository.countConfirmedParticipantsByInterviewIdAndInterviewerIdAndConfirmedAttendanceTrue(interviewId,interviewerId);
//    }
//
//
//
//    public Boolean isParticipantConfirmed(Long interviewId, Long participantId) {
//        // Verify the interview belongs to the interviewer
//        if (!interviewRepository.existsByIdAndInterviewerId(interviewId, interviewerId)) {
//            throw new ResourceNotFoundException("Interview not found with id: " + interviewId);
//        }
//
//        InterviewParticipant participant = participantRepository
//                .findByInterviewIdAndParticipantIdAndInterviewerId(interviewId, participantId, interviewerId)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Participant not found with interviewId: " + interviewId +
//                                ", participantId: " + participantId +
//                                ", and interviewerId: " + interviewerId
//                ));
//        return participant.getConfirmedAttendance();
//    }
//
//
////
////    @Transactional
////    public ParticipantResponseDTO fullUpdateParticipant(Long id, ParticipantRequestDTO requestDTO) {
////        InterviewParticipant participant = participantRepository.findById(id)
////                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
////
////        // Verify the interview belongs to the interviewer
////        if (!participant.getInterview().getInterviewerId().equals(interviewerId)) {
////            throw new ResourceNotFoundException("Participant not found with id: " + id);
////        }
////
////        // For full update, we might need to check if interview exists if it's being changed
////        if (!participant.getInterview().getId().equals(requestDTO.getInterviewId())) {
////            Interview newInterview = interviewRepository.findByIdAndInterviewerId(requestDTO.getInterviewId(), interviewerId)
////                    .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + requestDTO.getInterviewId()));
////            participant.setInterview(newInterview);
////        }
////
////        // Use mapper for full update
////        participantMapper.updateEntityFromRequestDTO(requestDTO, participant);
////
////        InterviewParticipant updatedParticipant = participantRepository.save(participant);
////        log.info("Fully updated participant with id: {}", id);
////
////        return participantMapper.toResponseDTO(updatedParticipant);
////    }
////
////    // Additional utility methods
////    public List<ParticipantResponseDTO> getParticipantsByInterviewAndType(Long interviewId, ParticipantType participantType) {
////        // Verify the interview belongs to the interviewer
////        if (!interviewRepository.existsByIdAndInterviewerId(interviewId, interviewerId)) {
////            throw new ResourceNotFoundException("Interview not found with id: " + interviewId);
////        }
////
////        List<InterviewParticipant> participants = participantRepository.findByInterviewIdAndParticipantType(interviewId, participantType);
////        return participants.stream()
////                .map(participantMapper::toResponseDTO)
////                .collect(Collectors.toList());
////    }
////
////    public List<ParticipantResponseDTO> getRequiredParticipants(Long interviewId) {
////        // Verify the interview belongs to the interviewer
////        if (!interviewRepository.existsByIdAndInterviewerId(interviewId, interviewerId)) {
////            throw new ResourceNotFoundException("Interview not found with id: " + interviewId);
////        }
////
//////        List<InterviewParticipant> participants = participantRepository.findByInterviewId(interviewId);
//////        return participants.stream()
//////                .filter(participant -> Boolean.TRUE.equals(participant.getIsRequired()))
//////                .map(participantMapper::toResponseDTO)
//////                .collect(Collectors.toList());
////        return null;
////    }
////
////    @Transactional
////    public void bulkConfirmAttendance(List<Long> participantIds) {
////        List<InterviewParticipant> participants = participantRepository.findAllById(participantIds);
////
////        // Filter only participants that belong to interviewer's interviews
////        List<InterviewParticipant> validParticipants = participants.stream()
////                .filter(participant -> participant.getInterview().getInterviewerId().equals(interviewerId))
////                .collect(Collectors.toList());
////
////        for (InterviewParticipant participant : validParticipants) {
////            participant.setConfirmedAttendance(true);
////        }
////
////        participantRepository.saveAll(validParticipants);
////        log.info("Bulk confirmed attendance for {} participants", validParticipants.size());
////    }
//}
