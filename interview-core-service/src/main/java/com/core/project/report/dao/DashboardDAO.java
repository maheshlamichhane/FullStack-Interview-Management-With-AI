package com.core.project.report.dao;//package com.itsutra.project.report.dao;
//
//
//import com.itsutra.project.report.entity.Dashboard;
//import com.itsutra.project.report.enums.DashboardCategory;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface DashboardDAO extends JpaRepository<Dashboard, Long> {
//
//    Optional<Dashboard> findByCode(String code);
//    Boolean existsByCodeAndCreatedById(String code,Long createdById);
//
//    Page<Dashboard> findByCategory(DashboardCategory category, Pageable pageable);
//    Page<Dashboard> findByIsActive(Boolean isActive, Pageable pageable);
//    Page<Dashboard> findByIsPublic(Boolean isPublic, Pageable pageable);
//
//    @Query("SELECT d FROM Dashboard d WHERE d.isActive = true AND d.isPublic = true")
//    List<Dashboard> findActivePublicDashboards();
//
//    @Query("SELECT d FROM Dashboard d JOIN d.shares s WHERE s.sharedWithUserId = :userId AND d.isActive = true")
//    List<Dashboard> findSharedDashboardsByUserId(@Param("userId") Long userId);
//
//    @Query("SELECT d FROM Dashboard d WHERE d.createdBy.id = :userId AND d.isActive = true")
//    List<Dashboard> findDashboardsByCreator(@Param("userId") Long userId);
//    Optional<Dashboard> findByIdAndCreatedById(Long id, Long createdById);
//
//    @Query("SELECT COUNT(d) FROM Dashboard d WHERE d.isActive = true")
//    Long countActiveDashboards();
//}
