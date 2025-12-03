package com.itsutra.project.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @Override
    public String sendSms(String to, String message) {
        try {
            Twilio.init(accountSid, authToken);

            Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(fromPhoneNumber),
                    message
            ).create();

            log.info("SMS sent successfully to: {}", to);
            return "SUCCESS";

        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", to, e.getMessage(), e);
            return "FAILED: " + e.getMessage();
        }
    }

    @Override
    public String sendBulkSms(List<String> recipients, String message) {
        // Implementation for bulk SMS
        return "SUCCESS";
    }
}
