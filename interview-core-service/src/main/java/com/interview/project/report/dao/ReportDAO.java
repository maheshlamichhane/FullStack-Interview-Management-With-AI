package com.interview.project.report.dao;//package com.itsutra.project.report.dao;
//
//import com.itsutra.project.report.entity.Report;
//import com.itsutra.project.report.enums.ReportCategory;
//import com.itsutra.project.report.enums.ReportType;
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
//public interface ReportDAO extends JpaRepository<Report, Long> {
//
//
//    Optional<Report> findByIdAndCreatedById(Long id, Long userId);
//    List<Report> findByCreatedById(Long createdById);
//    Optional<Report> findByCodeAndCreatedById(String code,Long createdById);
//    boolean existsByCodeAndCreatedById(String code, Long createdById);
//    Page<Report> findByCategory(ReportCategory category, Pageable pageable);
//    Page<Report> findByReportType(ReportType reportType, Pageable pageable);
//    Page<Report> findByIsActive(Boolean isActive, Pageable pageable);
//    Page<Report> findByIsPublic(Boolean isPublic, Pageable pageable);
//    Page<Report> findByIsScheduled(Boolean isScheduled, Pageable pageable);
//
//    @Query("SELECT r FROM Report r WHERE r.isActive = true AND r.isScheduled = true AND r.nextRunAt <= CURRENT_TIMESTAMP")
//    List<Report> findScheduledReportsDueForExecution();
//
//    @Query("SELECT r FROM Report r WHERE r.category = :category AND r.isActive = true AND r.isPublic = true")
//    List<Report> findActivePublicReportsByCategory(@Param("category") ReportCategory category);
//
//    @Query("SELECT COUNT(r) FROM Report r WHERE r.isActive = true")
//    Long countActiveReports();
//
//    @Query("SELECT r.category, COUNT(r) FROM Report r WHERE r.isActive = true GROUP BY r.category")
//    List<Object[]> countReportsByCategory();
//}
