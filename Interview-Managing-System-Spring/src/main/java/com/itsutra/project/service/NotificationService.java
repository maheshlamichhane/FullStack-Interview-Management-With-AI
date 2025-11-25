package com.itsutra.project.service;


import com.itsutra.project.entity.Notification;
import com.itsutra.project.dao.NotificationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    public Notification createNotification(String type, String title, String message, String interviewId) {
        Notification notification = new Notification(type, title, message, interviewId);
        return notificationDao.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationDao.findByOrderByCreatedAtDesc();
    }

    public List<Notification> getUnreadNotifications() {
        return notificationDao.findByIsReadFalseOrderByCreatedAtDesc();
    }

    public long getUnreadCount() {
        return notificationDao.countByIsReadFalse();
    }

    public void markAsRead(List<Long> notificationIds) {
        notificationDao.markAsRead(notificationIds);
    }

    public void markAllAsRead() {
        notificationDao.markAllAsRead();
    }

    public void deleteNotification(Long id) {
        notificationDao.deleteById(id);
    }

    public void clearAllNotifications() {
        notificationDao.deleteAll();
    }
}
