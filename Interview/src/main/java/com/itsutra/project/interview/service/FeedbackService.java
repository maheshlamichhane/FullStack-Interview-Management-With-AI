//package com.itsutra.project.interview.service;
//
//
//import com.itsutra.project.interview.dao.FeedbackRepository;
//import com.itsutra.project.interview.dto.FeedbackRequest;
//import com.itsutra.project.interview.dto.FeedbackResponse;
//import com.itsutra.project.interview.dto.FeedbackUpdateRequest;
//import com.itsutra.project.interview.entity.Feedback;
//import com.itsutra.project.interview.entity.Interview;
//import com.itsutra.project.interview.exception.ResourceNotFoundException;
//import com.itsutra.project.interview.mapper.InterviewMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class FeedbackService {
//
//    private final FeedbackRepository feedbackRepository;
//    private final InterviewService interviewService;
//    private final InterviewMapper interviewMapper;
//    private final Long interviewerId = 109l;
//
//
//
//    @Transactional
//    public FeedbackResponse createFeedback(FeedbackRequest request) {
//
//        Interview interview = interviewService.findInterviewByIdAndParticipantId(request.getInterviewId(),interviewerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with interview id and participation id: " + request.getInterviewId()+" "+interviewerId));
//
//        // also verify provided for
//        long providedFor = request.getProvidedFor();
//        long candidateId = interview.getCandidateId();
//        if(providedFor != candidateId) {
//            throw new IllegalStateException("Provided for mismatch between interview and candidate for interview");
//        }
//
//        // Check if feedback already exists from this provider
//        feedbackRepository.findByInterviewIdAndProvidedBy(request.getInterviewId(),interviewerId)
//                .ifPresent(existing -> {
//                    throw new IllegalStateException("Feedback already provided by this user for the interview");
//                });
//
//        Feedback feedback = interviewMapper.toFeedbackEntity(request, interview,interviewerId);
//        Feedback savedFeedback = feedbackRepository.save(feedback);
//
//        // Update interview overall rating if this is final feedback
//        if (savedFeedback.getIsFinalFeedback()) {
//            updateInterviewOverallRating(interview);
//        }
//
//        log.info("Created feedback for interview: {} by user: {}", request.getInterviewId(),interviewerId);
//        return interviewMapper.toFeedbackResponse(savedFeedback);
//    }

//    @Transactional
//    public List<FeedbackResponse> getFeedbacksByInterview(Long interviewId) {
//        List<Feedback> feedbacks = feedbackRepository.findByInterviewId(interviewId);
//        return feedbacks.stream()
//                .map(interviewMapper::toFeedbackResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public List<FeedbackResponse> getFeedbacksByProvider(Long providerId) {
//        List<Feedback> feedbacks = feedbackRepository.findByProvidedBy(providerId);
//        return feedbacks.stream()
//                .map(interviewMapper::toFeedbackResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public List<FeedbackResponse> getFinalFeedbackByInterview(Long interviewId) {
//        List<Feedback> feedback = feedbackRepository.findByInterviewIdAndIsFinalFeedbackTrue(interviewId);
//        if(feedback.isEmpty()) {
//            throw new ResourceNotFoundException("Feedback not found with status true and interview id " + interviewId);
//        }
//        return feedback.stream().map((interviewMapper::toFeedbackResponse)).collect(Collectors.toList());
//    }
//
//
//
//
//
//    @Transactional
//    public FeedbackResponse updateFeedback(Long feedbackId, FeedbackUpdateRequest request) {
//
//        Feedback feedback = feedbackRepository.findById(feedbackId)
//                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + feedbackId));
//
//
//        if (request.getTechnicalSkillsRating() != null) {
//            feedback.setTechnicalSkillsRating(request.getTechnicalSkillsRating());
//        }
//
//        if (request.getCommunicationSkillsRating() != null) {
//            feedback.setCommunicationSkillsRating(request.getCommunicationSkillsRating());
//        }
//        if (request.getProblemSolvingRating() != null) {
//            feedback.setProblemSolvingRating(request.getProblemSolvingRating());
//        }
//        if (request.getCulturalFitRating() != null) {
//            feedback.setCulturalFitRating(request.getCulturalFitRating());
//        }
//        if (request.getOverallRating() != null) {
//            feedback.setOverallRating(request.getOverallRating());
//        }
//        if (request.getStrengths() != null) {
//            feedback.setStrengths(request.getStrengths());
//        }
//        if (request.getAreasForImprovement() != null) {
//            feedback.setAreasForImprovement(request.getAreasForImprovement());
//        }
//        if (request.getComments() != null) {
//            feedback.setComments(request.getComments());
//        }
//        if (request.getRecommendation() != null) {
//            feedback.setRecommendation(request.getRecommendation());
//        }
//        if (request.getIsFinalFeedback() != null) {
//            feedback.setIsFinalFeedback(request.getIsFinalFeedback());
//        }
//        if (request.getIsSharedWithCandidate() != null) {
//            feedback.setIsSharedWithCandidate(request.getIsSharedWithCandidate());
//        }
//
//        Feedback updatedFeedback = feedbackRepository.save(feedback);
//
//        // Update interview overall rating if this is final feedback
//        if (updatedFeedback.getIsFinalFeedback()) {
//            updateInterviewOverallRating(updatedFeedback.getInterview());
//        }
//
//        log.info("Updated feedback with id: {}", feedbackId);
//        return interviewMapper.toFeedbackResponse(updatedFeedback);
//    }
//
//
//    @Transactional
//    public void deleteFeedback(Long feedbackId) {
//        Feedback feedback = feedbackRepository.findById(feedbackId)
//                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + feedbackId));
//        feedbackRepository.delete(feedback);
//        log.info("Deleted feedback with id: {}", feedbackId);
//    }
//
//
//
//    @Transactional
//    public FeedbackResponse markAsFinalFeedback(Long feedbackId) {
//        Feedback feedback = feedbackRepository.findById(feedbackId)
//                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + feedbackId));
//
//
//        feedback.setIsFinalFeedback(true);
//        Feedback updatedFeedback = feedbackRepository.save(feedback);
//
//        // Update interview overall rating
//        updateInterviewOverallRating(feedback.getInterview());
//
//        log.info("Marked feedback {} as final for interview {}", feedbackId, feedback.getInterview().getId());
//        return interviewMapper.toFeedbackResponse(updatedFeedback);
//    }
//
//
//
//    @Transactional
//    public FeedbackResponse shareFeedbackWithCandidate(Long feedbackId) {
//        Feedback feedback = feedbackRepository.findById(feedbackId)
//                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + feedbackId));
//
//        feedback.setIsSharedWithCandidate(true);
//        Feedback updatedFeedback = feedbackRepository.save(feedback);
//
//        log.info("Shared feedback {} with candidate {}", feedbackId, feedback.getProvidedFor());
//        return interviewMapper.toFeedbackResponse(updatedFeedback);
//    }
//
//    @Transactional
//    public Double getCandidateAverageRating(Long candidateId) {
//        return feedbackRepository.findAverageRatingByCandidate(candidateId);
//    }





//    // Helper method
//    private void updateInterviewOverallRating(Interview interview) {
//        List<Feedback> finalFeedbacks = feedbackRepository.findByInterviewId(interview.getId()).stream()
//                .filter(Feedback::getIsFinalFeedback)
//                .collect(Collectors.toList());
//
//        if (!finalFeedbacks.isEmpty()) {
//            double averageRating = finalFeedbacks.stream()
//                    .mapToInt(Feedback::getOverallRating)
//                    .average()
//                    .orElse(0.0);
//            interview.setOverallRating(averageRating);
//            interviewService.saveInterview(interview);
//        }
//    }
//}
