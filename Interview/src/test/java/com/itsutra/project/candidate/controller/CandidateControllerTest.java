package com.itsutra.project.candidate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsutra.project.candidate.dto.CandidateRequestDTO;
import com.itsutra.project.candidate.dto.CandidateResponseDTO;
import com.itsutra.project.candidate.service.CandidateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CandidateController.class)
public class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService candidateService;


    @Test
    @DisplayName("Candidate Creation Test")
    public void testCreateCandidate() throws Exception {

        // Arrange
        CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();
        candidateRequestDTO.setEmail("demo@gmail.com");
        candidateRequestDTO.setFirstName("John");
        candidateRequestDTO.setLastName("Doe");

        CandidateResponseDTO response = new CandidateResponseDTO();
        response.setEmail("demo@gmail.com");
        response.setFirstName("John");
        response.setLastName("Doe");


        when(candidateService.createCandidate(any(CandidateRequestDTO.class))).thenReturn(response);

        RequestBuilder requestBuilder =  MockMvcRequestBuilders.post("/api/interviews/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(candidateRequestDTO));


        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        CandidateResponseDTO candidateResponseDTO = new ObjectMapper().readValue(responseBodyAsString, CandidateResponseDTO.class);

        // Assert
        Assertions.assertEquals(candidateResponseDTO.getEmail(), candidateRequestDTO.getEmail());
        Assertions.assertEquals(candidateResponseDTO.getFirstName(),candidateRequestDTO.getFirstName());
        Assertions.assertEquals(candidateResponseDTO.getLastName(),candidateRequestDTO.getLastName());

    }




    @Test
    @DisplayName("Candidate Request Mandatory Field Test")
    public void testEmptyValueForCandidateRequest() throws Exception {

        // Arrange
        CandidateRequestDTO candidateRequestDTO = new CandidateRequestDTO();
        candidateRequestDTO.setEmail("demo@gmail.com");
        candidateRequestDTO.setFirstName("");
        candidateRequestDTO.setLastName("Doe");

        CandidateResponseDTO response = new CandidateResponseDTO();
        response.setEmail("demo@gmail.com");
        response.setFirstName("John");
        response.setLastName("Doe");

        when(candidateService.createCandidate(any(CandidateRequestDTO.class))).thenReturn(response);

        RequestBuilder requestBuilder =  MockMvcRequestBuilders.post("/api/interviews/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(candidateRequestDTO));


        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),mvcResult.getResponse().getStatus(),"Incorrect Http status code");

    }



    @Test
    @DisplayName("Candidate Get By Id Test")
    public void testGetCandidateByIdTest() throws Exception {

        // Arrange
        CandidateResponseDTO response = new CandidateResponseDTO();
        response.setId(1L);
        response.setEmail("demo@gmail.com");
        response.setFirstName("John");
        response.setLastName("Doe");


        when(candidateService.getCandidateById(1L)).thenReturn(response);

        RequestBuilder requestBuilder =  MockMvcRequestBuilders.get("/api/interviews/candidates/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON);


        MvcResult mvcResult = mockMvc.perform(requestBuilder)
        .andExpect(status().isOk())
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        CandidateResponseDTO candidateResponseDTO = new ObjectMapper().readValue(responseBodyAsString, CandidateResponseDTO.class);

        Assertions.assertEquals(candidateResponseDTO.getId(), response.getId());
    }
}
