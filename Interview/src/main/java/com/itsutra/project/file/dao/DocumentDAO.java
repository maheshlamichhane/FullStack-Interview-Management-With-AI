//package com.itsutra.project.file.dao;
//
//import com.itsutra.project.file.entity.Document;
//import com.itsutra.project.file.enums.DocumentCategory;
//import com.itsutra.project.file.enums.DocumentStatus;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface DocumentDAO extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {
//
//    List<Document> findByCreatedById(Long id);
//    Optional<Document> findByIdAndCreatedById(Long id, Long createdById);
//    List<Document> findByCategory(DocumentCategory category);
//    Page<Document> findByStatus(DocumentStatus status, Pageable pageable);
//    Page<Document> findByDocumentType(String documentType, Pageable pageable);
//    Page<Document> findByCreatedById(Long createdById, Pageable pageable);
//    Page<Document> findByIsConfidential(Boolean isConfidential, Pageable pageable);
//
//    @Query("SELECT d FROM Document d WHERE d.parentDocumentId = :parentId ORDER BY d.version DESC")
//    List<Document> findVersionsByParentId(@Param("parentId") Long parentId);
//
//    List<Document> findByParentDocumentIdAndCreatedById(Long id, Long createdById);
//
//    @Query("SELECT d FROM Document d WHERE d.tags LIKE %:tag% and d.createdBy.id= :createdById")
//    List <Document> getByTagAndCreatedIdInfo(@Param("tag") String tag, @Param("createdById") Long createdById);
//
//    List<Document> findByTagsAndCreatedById(String tag, Long createdById);
//    @Query("SELECT d FROM Document d WHERE d.isVerified = true AND d.status = 'APPROVED'")
//    Page<Document> findVerifiedDocuments(Pageable pageable);
//
//    @Query("SELECT DISTINCT d.documentType FROM Document d")
//    List<String> findDistinctDocumentTypes();
//
//    @Query("SELECT d.category, COUNT(d) FROM Document d GROUP BY d.category")
//    List<Object[]> countDocumentsByCategory();
//}
