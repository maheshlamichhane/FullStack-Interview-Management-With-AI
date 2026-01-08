//package com.itsutra.project.file.dao;
//
//
//import com.itsutra.project.file.entity.UploadSession;
//import com.itsutra.project.file.enums.UploadStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface UploadSessionDAO extends JpaRepository<UploadSession, Long> {
//
//    Optional<UploadSession> findBySessionId(String sessionId);
//    Optional<UploadSession> findByUploadId(String uploadId);
//    List<UploadSession> findByStatus(UploadStatus status);
//
//    @Query("SELECT us FROM UploadSession us WHERE us.expiresAt < :currentDate AND us.status IN ('INITIATED', 'IN_PROGRESS')")
//    List<UploadSession> findExpiredSessions(@Param("currentDate") LocalDateTime currentDate);
//
//    @Query("SELECT us FROM UploadSession us WHERE us.file.id = :fileId")
//    List<UploadSession> findByFileId(@Param("fileId") Long fileId);
//
//    @Query("SELECT us FROM UploadSession us WHERE us.createdAt < :thresholdDate")
//    List<UploadSession> findOldSessions(@Param("thresholdDate") LocalDateTime thresholdDate);
//}
