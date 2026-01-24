package com.core.project.job.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {


    @Id
    private Long id;

    @NotBlank
    private String name;

    private String code;

    private String description;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_department_id")
//    private Department parentDepartment;

//    @OneToMany(mappedBy = "parentDepartment", fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<Department> childDepartments = new ArrayList<>();

    private Long managerId;

    @Builder.Default
    private Boolean isActive = true;

    private String budgetCode;

    private String costCenter;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

//    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<JobPosition> jobPositions = new ArrayList<>();

    // Helper methods
//    public Boolean hasParent() {
//        return parentDepartment != null;
//    }
//
//    public Boolean hasChildren() {
//        return !childDepartments.isEmpty();
//    }
}
