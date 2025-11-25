package com.itsutra.project.dao;

import com.itsutra.project.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationDao extends JpaRepository<Notification, Long> {

    List<Notification> findByOrderByCreatedAtDesc();

    List<Notification> findByIsReadFalseOrderByCreatedAtDesc();

    long countByIsReadFalse();

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id IN :ids")
    void markAsRead(@Param("ids") List<Long> ids);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true")
    void markAllAsRead();
}
