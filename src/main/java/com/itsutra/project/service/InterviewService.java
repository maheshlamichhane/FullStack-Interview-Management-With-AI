package com.itsutra.project.service;


import com.itsutra.project.dto.CreateInterviewRequest;
import com.itsutra.project.dto.CreateInterviewResponse;
import com.itsutra.project.entity.Interview;

import java.util.List;


public interface InterviewService {
    void createInterview(CreateInterviewRequest createInterviewRequest, String username);
    List<CreateInterviewResponse> findAllInterviewsInformation();
    CreateInterviewResponse findInterview(long id) throws Exception;
    void deleteInterview(long id) throws Exception;
    void updateInterview(Interview interview) throws Exception;
}
