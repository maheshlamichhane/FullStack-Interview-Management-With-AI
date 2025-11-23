package com.itsutra.project.service;

import com.itsutra.project.dao.FeedbackDao;
import com.itsutra.project.dao.InterviewDao;
import com.itsutra.project.dao.ParticipantDao;
import com.itsutra.project.dto.*;
import com.itsutra.project.entity.Interview;
import com.itsutra.project.entity.InterviewFeedback;
import com.itsutra.project.entity.InterviewParticipant;
import com.itsutra.project.mapper.FeedbackMapper;
import com.itsutra.project.mapper.InterviewMapper;
import com.itsutra.project.mapper.InterviewParticipantMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private InterviewMapper interviewMapper;
    private FeedbackMapper feedbackMapper;

    private InterviewParticipantMapper interviewParticipantMapper;


     private InterviewDao interviewDao;
     private FeedbackDao feedbackDao;
     private ParticipantDao participantDao;


    @Override
    @Transactional
    public void createInterview(CreateInterviewRequest createInterviewRequest, String username) {
        Interview interview = interviewMapper.toEntity(createInterviewRequest);
        interview.setCreatedBy(username);

        for (ParticipantRequest participantRequest : createInterviewRequest.getParticipants()) {
            InterviewParticipant participant = interviewParticipantMapper.toEntity(participantRequest);
            interview.addParticipant(participant);

            InterviewFeedback feedback = new InterviewFeedback();
            feedback.setParticipant(participant);
            interview.addFeedback(feedback);
        }
        interviewDao.save(interview);
    }

    @Override
    @Transactional
    public List<InterviewResponse> findAllInterviewsInformation() {
        List<Interview> interviews = interviewDao.findAllWithParticipants();

        if (interviews.isEmpty()) return Collections.emptyList();

        List<Long> interviewIds = interviews.stream()
                .map(Interview::getId)
                .collect(Collectors.toList());

        List<InterviewFeedback> feedbacks = feedbackDao.findByInterviewIdIn(interviewIds);
        Map<Long, List<InterviewFeedback>> feedbacksByInterview = feedbacks.stream()
                .collect(Collectors.groupingBy(f -> f.getInterview().getId()));

        return interviews.stream()
                .map(interview -> {
                    InterviewResponse response = interviewMapper.toResponse(interview);

                    // Add feedbacks
                    List<InterviewFeedback> interviewFeedbacks = feedbacksByInterview.get(interview.getId());
                    if (interviewFeedbacks != null) {
                        List<FeedbackResponse> feedbackResponses = interviewFeedbacks.stream()
                                .map(obj -> feedbackMapper.toResponse(obj))
                                .collect(Collectors.toList());
                        response.setFeedBackResponses(feedbackResponses);
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }


    @Override
    public InterviewResponse findInterview(long id) throws Exception {
        Interview interview = interviewDao.findByIdWithParticipants(id);
        if(interview == null){
            throw new Exception("Interview Not Found With Provided Id "+id);
        }

        InterviewResponse finalResponse = interviewMapper.toResponse(interview);

        List<FeedbackResponse> feedbackResponses = new ArrayList<>();
        interview.getParticipants().forEach(participant -> {
             List<InterviewFeedback> response = feedbackDao.getByInterviewIdAndParticipationId(interview.getId(),participant.getId());
             for(InterviewFeedback feedback : response) {
                 feedbackResponses.add(feedbackMapper.toResponse(feedback));
             }
        });
        finalResponse.setFeedBackResponses(feedbackResponses);
        return finalResponse;
    }

    @Override
    public void deleteInterview(long id) throws Exception {
        findInterview(id);
        interviewDao.deleteById(id);
    }


    @Override
    public void updateInterview(UpdateInterviewRequest request) throws Exception {
        Optional<Interview> interviewOptional = interviewDao.findById(request.getId());
        if(interviewOptional.isEmpty()){
            throw new Exception("Interview Not Found With Provided Id "+request.getId());
        }
        Interview interview = interviewMapper.toEntity(request,interviewOptional.get());
        List<ParticipantResponse> list = request.getParticipants();
        List<InterviewParticipant> interviewParticipants = new ArrayList<>();
        for(ParticipantResponse participant : list){
            InterviewParticipant interviewParticipant = interviewParticipantMapper.toEntity(participant);
            interviewParticipant.setInterview(interview);
            interviewParticipants.add(interviewParticipant);
        }
        interview.setParticipants(interviewParticipants);
        interviewDao.save(interview);
    }


}
