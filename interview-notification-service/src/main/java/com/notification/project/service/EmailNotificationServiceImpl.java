package com.notification.project.service;

import com.notification.project.dao.NotificationHistoryRepository;
import com.notification.project.dao.NotificationTemplateRepository;
import com.notification.project.dto.NotificationRequest;
import com.notification.project.mapper.NotificationMapper;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Qualifier("emailNotificationService")
public class EmailNotificationServiceImpl extends AbstractNotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailNotificationServiceImpl(NotificationMapper notificationMapper,
                                        NotificationHistoryRepository notificationHistoryRepository,
                                        NotificationTemplateRepository notificationTemplateRepository,
                                        JavaMailSender mailSender) {
        super(notificationMapper, notificationHistoryRepository, notificationTemplateRepository);
        this.mailSender = mailSender;
    }

    @Override
    public Mono<String> sendNotification(NotificationRequest request, String body) {
        return Mono.fromCallable(() -> {
                    MimeMessage message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setFrom(fromEmail);
                    helper.setTo(request.getRecipient());
                    helper.setSubject(request.getSubject());
                    helper.setText(body, true);
                    mailSender.send(message);
                    log.info("Email sent successfully to {}", request.getRecipient());
                    return "SUCCESS";
                })
                .onErrorResume(e -> {
                    log.error("Failed to send email to {}: {}", request.getRecipient(), e.getMessage(), e);
                    return Mono.just("FAILED: " + e.getMessage());
                });
    }
}
