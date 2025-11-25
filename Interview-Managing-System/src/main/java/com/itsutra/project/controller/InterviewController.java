package com.itsutra.project.controller;


import com.itsutra.project.dto.CreateInterviewRequest;
import com.itsutra.project.dto.InterviewResponse;
import com.itsutra.project.dto.SimpleSuccessResponse;
import com.itsutra.project.dto.UpdateInterviewRequest;
import com.itsutra.project.mapper.InterviewMapper;
import com.itsutra.project.service.InterviewService;
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
    private InterviewService interviewServiceImpl;

    @Autowired
    private InterviewMapper interviewMapper;


    @PostMapping
    public ResponseEntity<SimpleSuccessResponse> createInterview(
            @Valid @RequestBody CreateInterviewRequest request) {

        interviewServiceImpl.createInterview(request,SecurityContextUtil.getCurrentUsername());
        return new ResponseEntity<>(new SimpleSuccessResponse("Interview Created Successfully"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InterviewResponse>> getAllInterview(){
        return ResponseEntity.status(HttpStatus.OK).body(interviewServiceImpl.findAllInterviewsInformation());
    }

    @GetMapping("/{id}")
    public InterviewResponse getInterviewById(@PathVariable("id") Long id) throws Exception {
        return interviewServiceImpl.findInterview(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleSuccessResponse> deleteInterviewById(@PathVariable("id") Long id) throws Exception {
        interviewServiceImpl.deleteInterview(id);
         SimpleSuccessResponse simple = new SimpleSuccessResponse();
         simple.setMessage("Interview deleted successfully");
         return ResponseEntity.status(HttpStatus.OK).body(simple);
    }


    @PutMapping
    public ResponseEntity<SimpleSuccessResponse> updateInterviewById(@Valid @RequestBody UpdateInterviewRequest request) throws Exception {
        interviewServiceImpl.updateInterview(request);
        return ResponseEntity.status(HttpStatus.OK).body(new SimpleSuccessResponse("Interview updated successfully"));
    }
}
