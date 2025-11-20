package com.itsutra.project.controller;


import com.itsutra.project.dto.CreateInterviewRequest;
import com.itsutra.project.dto.CreateInterviewResponse;
import com.itsutra.project.dto.SimpleSuccessResponse;
import com.itsutra.project.entity.Interview;
import com.itsutra.project.mapper.InterviewMapper;
import com.itsutra.project.service.InterviewServiceImpl;
import com.itsutra.project.utilities.SecurityContextUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/interviews")
@Validated
public class InterviewController {

    @Autowired
    private InterviewServiceImpl interviewService;

    @Autowired
    private InterviewMapper interviewMapper;


    @PostMapping
    public ResponseEntity<CreateInterviewResponse> createInterview(
            @Valid @RequestBody CreateInterviewRequest request) {

        Interview interview = interviewService.createInterview(request,SecurityContextUtil.getCurrentUsername());
        CreateInterviewResponse response = interviewMapper.toResponse(interview);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CreateInterviewResponse>> getAllInterview(){
        return ResponseEntity.status(HttpStatus.OK).body(interviewService.findAllInterviewsInformation());
    }

    @GetMapping("/{id}")
    public CreateInterviewResponse getInterviewById(@PathVariable("id") Long id) throws Exception {
        return interviewService.findInterview(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleSuccessResponse> deleteInterviewById(@PathVariable("id") Long id) throws Exception {
         interviewService.deleteInterview(id);
         SimpleSuccessResponse simple = new SimpleSuccessResponse();
         simple.setMessage("Interview deleted successfully");
         return ResponseEntity.status(HttpStatus.OK).body(simple);
    }


    @PutMapping
    public void updateInterviewById(@Valid @RequestBody CreateInterviewRequest request) throws Exception {
//        interviewService.updateInterview(request);
    }

}
