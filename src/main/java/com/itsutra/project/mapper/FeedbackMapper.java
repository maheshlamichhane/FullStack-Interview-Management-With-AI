package com.itsutra.project.mapper;

import com.itsutra.project.dto.FeedbackResponse;
import com.itsutra.project.entity.InterviewFeedback;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public FeedbackResponse toResponse(InterviewFeedback interviewFeedback) {
        FeedbackResponse response = new FeedbackResponse();
        response.setId(interviewFeedback.getId());
        response.setFeedbackText(interviewFeedback.getFeedbackText());
        response.setRating(interviewFeedback.getRating());
        response.setStrengths(interviewFeedback.getStrengths());
        response.setAreasForImprovement(interviewFeedback.getAreasForImprovement());
        response.setRecommendation(interviewFeedback.getRecommendation());
        response.setSubmittedAt(interviewFeedback.getSubmittedAt());
        return  response;
    }
}
