package com.itsutra.project.interview.service;

import com.itsutra.project.interview.dao.InterviewParticipantRepository;
import com.itsutra.project.interview.entity.InterviewParticipant;
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
