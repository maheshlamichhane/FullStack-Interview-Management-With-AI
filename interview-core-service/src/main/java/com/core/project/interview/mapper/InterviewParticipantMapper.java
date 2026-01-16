package com.core.project.interview.mapper;//package com.itsutra.project.interview.mapper;
//
//
//import com.itsutra.project.interview.dto.ParticipantRequestDTO;
//import org.springframework.stereotype.Component;
//
//@Component
//public class InterviewParticipantMapper {

//    public InterviewParticipant toEntity(ParticipantRequestDTO requestDTO, Interview interview,Long interviewerId) {
//        if (requestDTO == null) {
//            return null;
//        }
//
//        return InterviewParticipant.builder()
//                .interview(interview)
//                .interviewerId(interviewerId)
//                .participantId(requestDTO.getParticipantId())
//                .participantType(requestDTO.getParticipantType())
//                .role(requestDTO.getRole())
//                .isRequired(requestDTO.getIsRequired() != null ? requestDTO.getIsRequired() : true)
//                .confirmedAttendance(requestDTO.getConfirmedAttendance() != null ? requestDTO.getConfirmedAttendance() : false)
//                .attended(requestDTO.getAttended())
//                .build();
//    }
//
//    public ParticipantResponseDTO toResponseDTO(InterviewParticipant participant) {
//        if (participant == null) {
//            return null;
//        }
//
//        ParticipantResponseDTO responseDTO = new ParticipantResponseDTO();
//        responseDTO.setId(participant.getId());
//        responseDTO.setInterviewId(participant.getInterview().getId());
//        responseDTO.setParticipantId(participant.getParticipantId());
//        responseDTO.setParticipantType(participant.getParticipantType());
//        responseDTO.setRole(participant.getRole());
//        responseDTO.setIsRequired(participant.getIsRequired());
//        responseDTO.setConfirmedAttendance(participant.getConfirmedAttendance());
//        responseDTO.setAttended(participant.getAttended());
//        responseDTO.setCreatedAt(participant.getCreatedAt());
//
//        return responseDTO;
//    }
//
//    public void updateEntityFromUpdateDTO(ParticipantUpdateDTO updateDTO, InterviewParticipant participant) {
//        if (updateDTO == null || participant == null) {
//            return;
//        }
//
//        if (updateDTO.getRole() != null) {
//            participant.setRole(updateDTO.getRole());
//        }
//        if (updateDTO.getIsRequired() != null) {
//            participant.setIsRequired(updateDTO.getIsRequired());
//        }
//        if (updateDTO.getConfirmedAttendance() != null) {
//            participant.setConfirmedAttendance(updateDTO.getConfirmedAttendance());
//        }
//        if (updateDTO.getAttended() != null) {
//            participant.setAttended(updateDTO.getAttended());
//        }
//    }
//
//    public void updateEntityFromRequestDTO(ParticipantRequestDTO requestDTO, InterviewParticipant participant) {
//        if (requestDTO == null || participant == null) {
//            return;
//        }
//
//        // Note: interview and participantId typically shouldn't be updated
//        if (requestDTO.getParticipantType() != null) {
//            participant.setParticipantType(requestDTO.getParticipantType());
//        }
//        if (requestDTO.getRole() != null) {
//            participant.setRole(requestDTO.getRole());
//        }
//        if (requestDTO.getIsRequired() != null) {
//            participant.setIsRequired(requestDTO.getIsRequired());
//        }
//        if (requestDTO.getConfirmedAttendance() != null) {
//            participant.setConfirmedAttendance(requestDTO.getConfirmedAttendance());
//        }
//        if (requestDTO.getAttended() != null) {
//            participant.setAttended(requestDTO.getAttended());
//        }
//    }
//}
