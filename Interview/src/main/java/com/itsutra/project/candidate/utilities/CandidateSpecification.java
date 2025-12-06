package com.itsutra.project.candidate.utilities;



import com.itsutra.project.candidate.dto.CandidateSearchRequestDTO;
import com.itsutra.project.candidate.entity.Candidate;
import com.itsutra.project.candidate.entity.CandidateSkill;
import com.itsutra.project.candidate.enums.EmploymentStatus;
import com.itsutra.project.candidate.enums.ProficiencyLevel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CandidateSpecification {

    public static Specification<Candidate> withSearchCriteria(CandidateSearchRequestDTO searchRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Name search (first name or last name)
            if (StringUtils.hasText(searchRequest.getName())) {
                String namePattern = "%" + searchRequest.getName().toLowerCase() + "%";
                Predicate firstNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")), namePattern
                );
                Predicate lastNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")), namePattern
                );
                predicates.add(criteriaBuilder.or(firstNamePredicate, lastNamePredicate));
            }

            // Email search
            if (StringUtils.hasText(searchRequest.getEmail())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + searchRequest.getEmail().toLowerCase() + "%"
                ));
            }

            // Skill search (join with CandidateSkill)
            if (StringUtils.hasText(searchRequest.getSkill())) {
                Join<Candidate, CandidateSkill> skillsJoin = root.join("skills", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(skillsJoin.get("skillName")),
                        searchRequest.getSkill().toLowerCase()
                ));
            }

            // Location search (current location or preferred location)
            if (StringUtils.hasText(searchRequest.getLocation())) {
                String locationPattern = "%" + searchRequest.getLocation().toLowerCase() + "%";
                Predicate currentLocationPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("currentLocation")), locationPattern
                );
                Predicate preferredLocationPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("preferredLocation")), locationPattern
                );
                predicates.add(criteriaBuilder.or(currentLocationPredicate, preferredLocationPredicate));
            }

            // Experience range search
            if (searchRequest.getMinExperience() != null || searchRequest.getMaxExperience() != null) {
                Path<Double> totalExperience = root.get("totalExperience");

                if (searchRequest.getMinExperience() != null && searchRequest.getMaxExperience() != null) {
                    predicates.add(criteriaBuilder.between(
                            totalExperience,
                            searchRequest.getMinExperience(),
                            searchRequest.getMaxExperience()
                    ));
                } else if (searchRequest.getMinExperience() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                            totalExperience, searchRequest.getMinExperience()
                    ));
                } else if (searchRequest.getMaxExperience() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(
                            totalExperience, searchRequest.getMaxExperience()
                    ));
                }
            }

            // Notice period search
            if (searchRequest.getMaxNoticePeriod() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("noticePeriod"), searchRequest.getMaxNoticePeriod()
                ));
            }

            // Employment status search
            if (searchRequest.getEmploymentStatus() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("employmentStatus"), searchRequest.getEmploymentStatus()
                ));
            }

            // Current company search
            if (StringUtils.hasText(searchRequest.getCurrentCompany())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("currentCompany")),
                        "%" + searchRequest.getCurrentCompany().toLowerCase() + "%"
                ));
            }

            // Willing to relocate search
            if (searchRequest.getWillingToRelocate() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("willingToRelocate"), searchRequest.getWillingToRelocate()
                ));
            }

            // Only active candidates (unless specifically searching for inactive)
            predicates.add(criteriaBuilder.equal(root.get("isActive"), true));

            // Combine all predicates with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Additional specific specifications for common search patterns

    public static Specification<Candidate> withSkills(List<String> skills) {
        return (root, query, criteriaBuilder) -> {
            if (skills == null || skills.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> skillPredicates = new ArrayList<>();
            Join<Candidate, CandidateSkill> skillsJoin = root.join("skills", JoinType.INNER);

            for (String skill : skills) {
                skillPredicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(skillsJoin.get("skillName")),
                        skill.toLowerCase()
                ));
            }

            return criteriaBuilder.or(skillPredicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Candidate> withExperienceRange(Double minYears, Double maxYears) {
        return (root, query, criteriaBuilder) -> {
            Path<Double> totalExperience = root.get("totalExperience");

            if (minYears != null && maxYears != null) {
                return criteriaBuilder.between(totalExperience, minYears, maxYears);
            } else if (minYears != null) {
                return criteriaBuilder.greaterThanOrEqualTo(totalExperience, minYears);
            } else if (maxYears != null) {
                return criteriaBuilder.lessThanOrEqualTo(totalExperience, maxYears);
            }

            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Candidate> withLocation(String location) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(location)) {
                return criteriaBuilder.conjunction();
            }

            String locationPattern = "%" + location.toLowerCase() + "%";
            Predicate currentLocation = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("currentLocation")), locationPattern
            );
            Predicate preferredLocation = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("preferredLocation")), locationPattern
            );

            return criteriaBuilder.or(currentLocation, preferredLocation);
        };
    }

    public static Specification<Candidate> withEmploymentStatus(EmploymentStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("employmentStatus"), status);
        };
    }

    public static Specification<Candidate> withNoticePeriod(Integer maxNoticePeriod) {
        return (root, query, criteriaBuilder) -> {
            if (maxNoticePeriod == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("noticePeriod"), maxNoticePeriod);
        };
    }

    public static Specification<Candidate> isActive() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), true);
    }

    public static Specification<Candidate> withCompany(String company) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(company)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("currentCompany")),
                    "%" + company.toLowerCase() + "%"
            );
        };
    }

    // Advanced search with skill proficiency level
    public static Specification<Candidate> withSkillAndProficiency(String skill, ProficiencyLevel proficiency) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(skill)) {
                return criteriaBuilder.conjunction();
            }

            Join<Candidate, CandidateSkill> skillsJoin = root.join("skills", JoinType.INNER);
            Predicate skillPredicate = criteriaBuilder.equal(
                    criteriaBuilder.lower(skillsJoin.get("skillName")),
                    skill.toLowerCase()
            );

            if (proficiency != null) {
                Predicate proficiencyPredicate = criteriaBuilder.equal(
                        skillsJoin.get("proficiencyLevel"), proficiency
                );
                return criteriaBuilder.and(skillPredicate, proficiencyPredicate);
            }

            return skillPredicate;
        };
    }

    // Search by multiple criteria with OR condition for skills
    public static Specification<Candidate> withAdvancedSearch(CandidateSearchRequestDTO searchRequest) {
        return Specification.where(withSearchCriteria(searchRequest))
                .and(withSkills(searchRequest.getSkills() != null ? searchRequest.getSkills() : List.of()));
    }
}
