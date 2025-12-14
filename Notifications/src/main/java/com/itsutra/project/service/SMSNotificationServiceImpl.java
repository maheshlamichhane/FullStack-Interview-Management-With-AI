package com.itsutra.project.service;

import com.itsutra.project.common.service.AuthenticationService;
import com.itsutra.project.dao.NotificationHistoryRepository;
import com.itsutra.project.dao.NotificationTemplateRepository;
import com.itsutra.project.dto.NotificationRequest;
import com.itsutra.project.mapper.NotificationMapper;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SMSNotificationServiceImpl extends AbstractNotificationService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    public SMSNotificationServiceImpl(NotificationMapper notificationMapper, NotificationHistoryRepository notificationHistoryRepository, NotificationTemplateRepository notificationTemplateRepository, AuthenticationService authenticationService) {
        super(notificationMapper, notificationHistoryRepository, notificationTemplateRepository, authenticationService);
    }

    @Override
    public String sendNotification(NotificationRequest notificationRequest, String body) {
        try {
            Twilio.init(accountSid, authToken);

            Message.creator(
                    new PhoneNumber(notificationRequest.getRecipient()),
                    new PhoneNumber(fromPhoneNumber),
                    body
            ).create();

            log.info("SMS sent successfully to: {}", notificationRequest.getRecipient());
            return "SUCCESS";

        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", notificationRequest.getRecipient(), e.getMessage(), e);
            return "FAILED: " + e.getMessage();
        }
    }


    //    @Override
//    public String sendBulkSms(List<String> recipients, String message) {
//        // Implementation for bulk SMS
//        return "SUCCESS";
//    }
}
