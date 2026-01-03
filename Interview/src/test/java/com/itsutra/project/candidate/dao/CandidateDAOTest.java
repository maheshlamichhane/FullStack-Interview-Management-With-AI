package com.itsutra.project.candidate.dao;

import com.itsutra.project.candidate.entity.Candidate;
import com.itsutra.project.candidate.enums.EmploymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CandidateDAOTest {

    @Autowired
    private CandidateDAO candidateDAO;

    @Autowired
    private TestEntityManager entityManager;

    private Candidate candidate1;
    private Candidate candidate2;

    @BeforeEach
    void setUp() {
        // Create two sample candidates
        candidate1 = Candidate.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .isActive(true)
                .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                .build();

        candidate2 = Candidate.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@example.com")
                .isActive(false)
                .employmentStatus(EmploymentStatus.SERVING_NOTICE)
                .build();

        // Persist using TestEntityManager
        entityManager.persist(candidate1);
        entityManager.persist(candidate2);
        entityManager.flush(); // force SQL execution
    }

    @Test
    void testFindByEmail() {
        assertThat(candidateDAO.findByEmail("john.doe@example.com")).isPresent()
                .get().extracting(Candidate::getFirstName).isEqualTo("John");

        assertThat(candidateDAO.findByEmail("nonexistent@example.com")).isEmpty();
    }

    @Test
    void testExistsByEmail() {
        assertThat(candidateDAO.existsByEmail("alice.smith@example.com")).isTrue();
        assertThat(candidateDAO.existsByEmail("unknown@example.com")).isFalse();
    }

    @Test
    void testFindByIsActive() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Candidate> activeCandidates = candidateDAO.findByIsActive(true, pageable);
        assertThat(activeCandidates.getTotalElements()).isEqualTo(1);
        assertThat(activeCandidates.getContent().get(0).getFirstName()).isEqualTo("John");

        Page<Candidate> inactiveCandidates = candidateDAO.findByIsActive(false, pageable);
        assertThat(inactiveCandidates.getTotalElements()).isEqualTo(1);
        assertThat(inactiveCandidates.getContent().get(0).getFirstName()).isEqualTo("Alice");
    }

    @Test
    void testFindByNameContaining() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Candidate> result1 = candidateDAO.findByNameContaining("john", pageable);
        assertThat(result1.getTotalElements()).isEqualTo(1);
        assertThat(result1.getContent().get(0).getEmail()).isEqualTo("john.doe@example.com");

        Page<Candidate> result2 = candidateDAO.findByNameContaining("Smith", pageable);
        assertThat(result2.getTotalElements()).isEqualTo(1);
        assertThat(result2.getContent().get(0).getEmail()).isEqualTo("alice.smith@example.com");

        Page<Candidate> result3 = candidateDAO.findByNameContaining("nonexistent", pageable);
        assertThat(result3.getTotalElements()).isEqualTo(0);
    }
}

