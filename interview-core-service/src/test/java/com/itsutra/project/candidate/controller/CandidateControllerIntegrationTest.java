//package com.itsutra.project.candidate.controller;
//
//import com.itsutra.project.candidate.dto.CandidateRequestDTO;
//import com.itsutra.project.candidate.entity.Candidate;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//@Transactional
//class CandidateControllerIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Test
//    void createCandidate_shouldPersistCandidateUsingEntityManager() {
//        // Arrange
//        CandidateRequestDTO request = CandidateRequestDTO.builder()
//                .firstName("Jane")
//                .lastName("Doe")
//                .email("jane.doe@example.com")
//                .phone("9876543210")
//                .linkedinUrl("https://linkedin.com/in/janedoe")
//                .githubUrl("https://github.com/janedoe")
//                .currentCompany("Tech Corp")
//                .currentPosition("Backend Developer")
//                .totalExperience(4.0)
//                .currentSalary(70000.0)
//                .expectedSalary(80000.0)
//                .noticePeriod(60)
//                .build();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        HttpEntity<CandidateRequestDTO> entity = new HttpEntity<>(request, headers);
//
//        // Act
//        ResponseEntity<?> response = restTemplate.postForEntity("/api/interviews/candidates", entity, Object.class);
//
//        // Assert HTTP response
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//
//        // Verify candidate is persisted using TestEntityManager
//        Candidate persistedCandidate = entityManager.createQuery(
//                        "SELECT c FROM Candidate c WHERE c.email = :email", Candidate.class)
//                .setParameter("email", "jane.doe@example.com")
//                .getSingleResult();
//
//        assertThat(persistedCandidate).isNotNull();
//        assertThat(persistedCandidate.getFirstName()).isEqualTo("Jane");
//        assertThat(persistedCandidate.getCurrentCompany()).isEqualTo("Tech Corp");
//    }
//}
//
//
