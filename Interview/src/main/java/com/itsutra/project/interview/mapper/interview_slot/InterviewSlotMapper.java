package com.itsutra.project.interview.mapper.interview_slot;

import com.itsutra.project.interview.dto.InterviewSlotRequest;
import com.itsutra.project.interview.dto.InterviewSlotResponse;
import com.itsutra.project.interview.entity.InterviewSlot;
import com.itsutra.project.interview.enums.SlotStatus;
import org.springframework.stereotype.Component;

@Component
public class InterviewSlotMapper {


    public InterviewSlot toInterviewSlotEntity(InterviewSlotRequest request, Long interviewerId) {
        return InterviewSlot.builder()
                .interviewerId(interviewerId)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(request.getStatus() != null ? request.getStatus() : SlotStatus.AVAILABLE)
                .build();
    }



    public InterviewSlotResponse toInterviewSlotResponse(InterviewSlot entity) {
        InterviewSlotResponse response = new InterviewSlotResponse();
        response.setId(entity.getId());
        response.setInterviewerId(entity.getInterviewerId());
        response.setStartTime(entity.getStartTime());
        response.setEndTime(entity.getEndTime());
        response.setStatus(entity.getStatus());
        response.setScheduledBy(entity.getScheduledBy());
        response.setCancelledBy(entity.getCancelledBy());
        response.setCancellationReason(entity.getCancellationReason());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}
