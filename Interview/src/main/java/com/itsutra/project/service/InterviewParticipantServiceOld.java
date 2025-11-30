package com.itsutra.project.service;

import com.itsutra.project.dao.InterviewParticipantRepository;
import com.itsutra.project.entity.InterviewParticipant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InterviewParticipantServiceOld {

    private InterviewParticipantRepository interviewParticipantRepository;


    @Transactional
    public void saveAllParticipants(List<InterviewParticipant> participants){
        interviewParticipantRepository.saveAll(participants);
    }

    @Transactional
    public void saveParticipants(InterviewParticipant participant){
        interviewParticipantRepository.save(participant);
    }


}
