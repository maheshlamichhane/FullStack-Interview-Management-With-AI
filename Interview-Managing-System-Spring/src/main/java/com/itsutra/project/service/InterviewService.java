package com.itsutra.project.service;


import com.itsutra.project.dto.CreateInterviewRequest;
import com.itsutra.project.dto.InterviewResponse;
import com.itsutra.project.dto.UpdateInterviewRequest;

import java.util.List;


public interface InterviewService {
    void createInterview(CreateInterviewRequest createInterviewRequest, String username);
    List<InterviewResponse> findAllInterviewsInformation();
    InterviewResponse findInterview(long id) throws Exception;
    void deleteInterview(long id) throws Exception;
    void updateInterview(UpdateInterviewRequest request) throws Exception;
}
