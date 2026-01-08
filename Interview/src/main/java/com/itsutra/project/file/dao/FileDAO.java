//package com.itsutra.project.file.dao;
//
//
//import com.itsutra.project.file.entity.File;
//import com.itsutra.project.file.enums.FileCategory;
//import com.itsutra.project.file.enums.FileStatus;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface FileDAO extends JpaRepository<File, Long>, JpaSpecificationExecutor<File> {
//
//    Optional<File> findByStorageKey(String storageKey);
//    Optional<File> findByCreatedByIdAndStorageKey(Long id, String storageKey);
////    Optional<File> findByCreatedById(Long createdBy, Specification<File> spec);
//    Optional<File> findByIdAndCreatedById(Long id, Long userId);
//    Boolean existsByStorageKey(String storageKey);
//
//    List<File> findByCategoryAndCreatedById(FileCategory category, Long id);
//    Page<File> findByStatus(FileStatus status, Pageable pageable);
//    Page<File> findByMimeTypeContaining(String mimeType, Pageable pageable);
//    Page<File> findByUploadedById(Long uploadedById, Pageable pageable);
//
//    @Query("SELECT f FROM File f WHERE f.expiresAt < :currentDate AND f.status = 'ACTIVE'")
//    List<File> findExpiredFiles(@Param("currentDate") LocalDateTime currentDate);
//
//    @Query("SELECT f FROM File f WHERE f.size > :minSize")
//    Page<File> findLargeFiles(@Param("minSize") Long minSize, Pageable pageable);
//
//    @Query("SELECT SUM(f.size) FROM File f WHERE f.status = 'ACTIVE'")
//    Long getTotalStorageUsed();
//
//    @Query("SELECT f.category, COUNT(f), SUM(f.size) FROM File f WHERE f.status = 'ACTIVE' GROUP BY f.category")
//    List<Object[]> getStorageUsageByCategory();
//
//    @Query("SELECT f FROM File f WHERE f.accessedAt < :thresholdDate AND f.status = 'ACTIVE'")
//    List<File> findInactiveFiles(@Param("thresholdDate") LocalDateTime thresholdDate);
//
//    @Query("SELECT f.storageProvider, COUNT(f), SUM(f.size) FROM File f WHERE f.status = 'ACTIVE' GROUP BY f.storageProvider")
//    List<Object[]> getStorageDistribution();
//}
