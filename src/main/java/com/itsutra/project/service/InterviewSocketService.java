package com.itsutra.project.service;

import com.itsutra.project.entity.Notification;
import com.itsutra.project.socket.InterviewWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterviewSocketService {

    @Autowired
    private InterviewWebSocketHandler webSocketHandler;

    @Autowired
    private NotificationService notificationService;

    public void sendInterviewUpdate(String interviewId, String updateType, Object data) {
        // Create notification in database
        String title = "Interview Updated";
        String message = String.format("Interview %s has been updated", interviewId);

        if ("created".equals(updateType)) {
            title = "New Interview Scheduled";
            message = "A new interview has been scheduled";
        } else if ("status_changed".equals(updateType)) {
            title = "Interview Status Changed";
            message = String.format("Interview status changed to %s", data);
        }

        Notification notification = notificationService.createNotification(
                "interview_" + updateType, title, message, interviewId
        );

        // Send via WebSocket
        String jsonMessage = String.format(
                "{\"type\": \"interview_%s\", \"interviewId\": \"%s\", \"title\": \"%s\", \"message\": \"%s\", \"notificationId\": %d, \"timestamp\": %d}",
                updateType, interviewId, title, message, notification.getId(), System.currentTimeMillis()
        );
        webSocketHandler.broadcastToAll(jsonMessage);
    }

    public void notifyParticipantAction(String interviewId, String participantName, String action) {
        String title = "Participant Action";
        String message = String.format("%s %s the interview", participantName, action);

        Notification notification = notificationService.createNotification(
                "participant_action", title, message, interviewId
        );

        String wsMessage = String.format(
                "{\"type\": \"participant_%s\", \"interviewId\": \"%s\", \"participant\": \"%s\", \"action\": \"%s\", \"notificationId\": %d, \"timestamp\": %d}",
                action, interviewId, participantName, action, notification.getId(), System.currentTimeMillis()
        );
        webSocketHandler.broadcastToAll(wsMessage);
    }
}
