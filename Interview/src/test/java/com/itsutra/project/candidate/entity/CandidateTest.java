//package com.itsutra.project.candidate.entity;
//
//import com.itsutra.project.candidate.entity.Candidate;
//import com.itsutra.project.candidate.enums.EmploymentStatus;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.ANY) // Use H2 in-memory DB for testing
//public class CandidateTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @DisplayName("Candidate Entity Test")
//    @Test
//    void testPersistCandidate() {
//        // Create a candidate
//        Candidate candidate = Candidate.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .phone("1234567890")
//                .employmentStatus(EmploymentStatus.ACTIVE)
//                .totalExperience(5.0)
//                .currentSalary(50000.0)
//                .expectedSalary(60000.0)
//                .noticePeriod(30)
//                .preferredLocation("New York")
//                .currentLocation("Boston")
//                .willingToRelocate(true)
//                .source("LinkedIn")
//                .build();
//
//        // Persist candidate using TestEntityManager
//        Candidate savedCandidate = entityManager.persistAndFlush(candidate);
//
//        // Verify ID is generated
//        assertThat(savedCandidate.getId()).isNotNull();
//
//        // Verify default values
//        assertThat(savedCandidate.getIsActive()).isTrue();
//        assertThat(savedCandidate.getCreatedAt()).isNotNull();
//        assertThat(savedCandidate.getUpdatedAt()).isNotNull();
//
//        // Verify the fields
//        assertThat(savedCandidate.getFirstName()).isEqualTo("John");
//        assertThat(savedCandidate.getEmploymentStatus()).isEqualTo(EmploymentStatus.ACTIVE);
//
//        // Verify empty lists for relationships
//        assertThat(savedCandidate.getResumes()).isEmpty();
//        assertThat(savedCandidate.getExperiences()).isEmpty();
//        assertThat(savedCandidate.getEducations()).isEmpty();
//        assertThat(savedCandidate.getSkills()).isEmpty();
//    }
//
////    @Test
////    void testCandidateWithResumes() {
////        Candidate candidate = Candidate.builder()
////                .firstName("Alice")
////                .lastName("Smith")
////                .email("alice.smith@example.com")
////                .build();
////
////        // Add resumes
////        candidate.getResumes().add(new Resume(null, "Resume1.pdf", candidate));
////        candidate.getResumes().add(new Resume(null, "Resume2.pdf", candidate));
////
////        // Persist candidate
////        Candidate savedCandidate = entityManager.persistAndFlush(candidate);
////
////        assertThat(savedCandidate.getResumes()).hasSize(2);
////        assertThat(savedCandidate.getResumes().get(0).getFileName()).isEqualTo("Resume1.pdf");
////    }
//}
//
