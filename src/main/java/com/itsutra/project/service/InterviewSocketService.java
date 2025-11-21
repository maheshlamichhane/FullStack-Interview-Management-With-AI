package com.itsutra.project.service;

import com.itsutra.project.dao.InterviewDao;
import com.itsutra.project.entity.Interview;
import com.itsutra.project.socket.InterviewWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class InterviewSocketService {

    @Autowired
    private InterviewWebSocketHandler webSocketHandler;

    @Autowired
    private InterviewDao interviewDao; // Your existing repository

    // Method called from REST API to broadcast interview updates
    public void notifyInterviewUpdate(Interview interview) {
        String message = String.format(
                "Interview updated: %s for position %s",
                interview.getId(),""

//                interview.getPosition()
        );
        webSocketHandler.broadcastMessage(message);
    }



    public void notifyNewInterview(Interview interview) {
        String message = String.format(
                "New interview scheduled: %s at %s","",""
//                interview.getCandidateName(),
//                interview.getInterviewTime()
        );
        webSocketHandler.broadcastMessage(message);
    }

    public Map<String, Object> getSocketStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("connectedClients", webSocketHandler.getConnectedClientsCount());
        status.put("timestamp", new Date());
        return status;
    }
}
