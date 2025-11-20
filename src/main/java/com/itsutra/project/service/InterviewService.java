package com.itsutra.project.service;


import com.itsutra.project.dto.CreateInterviewRequest;
import com.itsutra.project.dto.CreateInterviewResponse;
import com.itsutra.project.entity.Interview;

import java.util.List;


public interface InterviewService {
    public Interview createInterview(CreateInterviewRequest createInterviewRequest, String username);
    public List<CreateInterviewResponse> findAllInterviewsInformation();
    public CreateInterviewResponse findInterview(long id) throws Exception;
    public void deleteInterview(long id) throws Exception;
    public void updateInterview(Interview interview) throws Exception;
}
