package com.itsutra.project.controller;

import com.itsutra.project.dto.NotificationRequest;
import com.itsutra.project.dto.NotificationResponse;
import com.itsutra.project.enums.NotificationType;
import com.itsutra.project.service.EmailNotificationServiceImpl;
import com.itsutra.project.service.NotificationService;
import com.itsutra.project.service.SMSNotificationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailNotificationServiceImpl emailNotificationServiceImpl;
    private final SMSNotificationServiceImpl smsNotificationServiceImpl;


    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> sendNotification(
            @Valid @RequestBody NotificationRequest request) {
        NotificationResponse response = getNotificationService(request.getType()).manageHistory(request);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/send-email")
    public ResponseEntity<NotificationResponse> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body) {

        NotificationRequest request = new NotificationRequest();
        request.setType(NotificationType.EMAIL);
        request.setRecipient(to);
        request.setSubject(subject);
//        request.setMessage(body);

//        NotificationResponse response = notificationService.sendNotification(request);
//        return ResponseEntity.ok(response);
        return null;
    }

    @PostMapping("/send-sms")
    public ResponseEntity<NotificationResponse> sendSms(
            @RequestParam String to,
            @RequestParam String message) {

        NotificationRequest request = new NotificationRequest();
        request.setType(NotificationType.SMS);
        request.setRecipient(to);
//        request.setMessage(message);

//        NotificationResponse response = notificationService.sendNotification(request);
//        return ResponseEntity.ok(response);
        return null;
    }

    @GetMapping("/history")
    public ResponseEntity<Page<NotificationResponse>> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Implementation for paginated history
        return ResponseEntity.ok(null);
    }

    private NotificationService getNotificationService(NotificationType type) {
        if(type == NotificationType.EMAIL) {
            return emailNotificationServiceImpl;
        }
        else if(type == NotificationType.SMS) {
            return smsNotificationServiceImpl;
        }
        else{
            throw new IllegalArgumentException("Invalid NotificationType");
        }
    }
}
