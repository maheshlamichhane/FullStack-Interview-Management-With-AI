package com.core.project.job.dao;//package com.itsutra.project.job.dao;


import com.core.project.job.entity.Department;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentDAO extends ReactiveCrudRepository<Department, Long> {
//
//    Optional<Department> findByName(String name);
//    Optional<Department> findByCode(String code);
    Mono<Boolean> existsByName(String name);
    Mono<Boolean> existsByCode(String code);
//
//    List<Department> findByParentDepartmentIsNull();
//    List<Department> findByParentDepartmentId(Long parentId);
//    List<Department> findByIsActive(Boolean isActive);
//
//    @Query("SELECT d FROM Department d WHERE d.parentDepartment IS NULL AND d.isActive = true")
//    List<Department> findRootDepartments();
//
//    @Query("SELECT d FROM Department d WHERE d.parentDepartment.id = :parentId AND d.isActive = true")
//    List<Department> findActiveChildDepartments(@Param("parentId") Long parentId);
//
//    @Query("SELECT COUNT(d) FROM Department d WHERE d.isActive = true")
//    Long countActiveDepartments();
//
//    @Query("SELECT d FROM Department d WHERE d.managerId = :managerId")
//    List<Department> findByManagerId(@Param("managerId") Long managerId);
}
