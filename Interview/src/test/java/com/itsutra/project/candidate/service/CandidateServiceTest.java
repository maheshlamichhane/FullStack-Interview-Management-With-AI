package com.itsutra.project.candidate.service;

import com.itsutra.project.candidate.dao.CandidateDAO;
import com.itsutra.project.candidate.dto.CandidateRequestDTO;
import com.itsutra.project.candidate.dto.CandidateResponseDTO;
import com.itsutra.project.candidate.entity.Candidate;
import com.itsutra.project.candidate.exception.ResourceNotFoundException;
import com.itsutra.project.candidate.mapper.CandidateMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {

    @Mock
    private CandidateDAO candidateDAO;

    @Mock
    private CandidateMapper candidateMapper;

    @InjectMocks
    private CandidateService candidateService;


    @Test
    @DisplayName("Create Candidate - Success")
    void testCreateCandidateSuccess() {

        // Arrange
        CandidateRequestDTO request = new CandidateRequestDTO();
        request.setEmail("demo@gmail.com");
        request.setFirstName("John");
        request.setLastName("Doe");

        Candidate candidateEntity = new Candidate();
        candidateEntity.setId(1L);
        candidateEntity.setEmail("demo@gmail.com");

        CandidateResponseDTO responseDTO = new CandidateResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEmail("demo@gmail.com");
        responseDTO.setFirstName("John");
        responseDTO.setLastName("Doe");

        when(candidateDAO.existsByEmail(request.getEmail())).thenReturn(false);
        when(candidateMapper.toCandidateEntity(request)).thenReturn(candidateEntity);
        when(candidateDAO.save(any(Candidate.class))).thenReturn(candidateEntity);
        when(candidateMapper.toCandidateResponse(candidateEntity)).thenReturn(responseDTO);

        // Act
        CandidateResponseDTO response = candidateService.createCandidate(request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("demo@gmail.com", response.getEmail());
        Assertions.assertEquals("John", response.getFirstName());

        verify(candidateDAO).existsByEmail(request.getEmail());
        verify(candidateDAO).save(candidateEntity);
    }

    @Test
    @DisplayName("Create Candidate - Email Already Exists")
    void testCreateCandidateAlreadyExists() {

        // Arrange
        CandidateRequestDTO request = new CandidateRequestDTO();
        request.setEmail("demo@gmail.com");

        when(candidateDAO.existsByEmail(request.getEmail())).thenReturn(true);

        // Act + Assert
        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> candidateService.createCandidate(request)
        );

        Assertions.assertTrue(exception.getMessage().contains("already exists"));

        verify(candidateDAO).existsByEmail(request.getEmail());
        verify(candidateDAO, never()).save(any());
    }

    @Test
    @DisplayName("Get Candidate By Id - Success")
    void testGetCandidateByIdSuccess() throws ResourceNotFoundException {

        // Arrange
        Long id = 1L;

        Candidate candidate = new Candidate();
        candidate.setId(id);
        candidate.setEmail("demo@gmail.com");

        CandidateResponseDTO responseDTO = new CandidateResponseDTO();
        responseDTO.setId(id);
        responseDTO.setEmail("demo@gmail.com");

        when(candidateDAO.findById(id)).thenReturn(Optional.of(candidate));
        when(candidateMapper.toCandidateResponse(candidate)).thenReturn(responseDTO);

        // Act
        CandidateResponseDTO response = candidateService.getCandidateById(id);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(id, response.getId());

        verify(candidateDAO).findById(id);
    }

    @Test
    @DisplayName("Get Candidate By Id - Not Found")
    void testGetCandidateByIdNotFound() {

        // Arrange
        Long id = 99L;
        when(candidateDAO.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> candidateService.getCandidateById(id)
        );

        verify(candidateDAO).findById(id);
    }
}
