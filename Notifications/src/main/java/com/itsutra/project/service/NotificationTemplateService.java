package com.itsutra.project.service;

import com.itsutra.project.common.entity.User;
import com.itsutra.project.common.service.AuthenticationService;
import com.itsutra.project.dao.NotificationTemplateRepository;
import com.itsutra.project.dto.TemplateRequest;
import com.itsutra.project.dto.TemplateResponse;
import com.itsutra.project.entity.NotificationTemplate;
import com.itsutra.project.mapper.NotificationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationTemplateService {

    private final NotificationTemplateRepository notificationTemplateRepository;
    private final NotificationMapper notificationMapper;
    private final AuthenticationService authenticationService;

    @Transactional
    public TemplateResponse createNotificationTemplate(TemplateRequest templateRequest) {

        NotificationTemplate template = notificationMapper.toTemplateEntity(templateRequest);
        User user = authenticationService.getCurrentUser();
        template.setCreatedBy(user);
        NotificationTemplate savedTemplate = notificationTemplateRepository.save(template);
        return notificationMapper.toTemplateResponse(savedTemplate);

    }

    @Transactional
    public TemplateResponse updateNotificationTemplate(TemplateRequest templateRequest,Long id) {
        User user = authenticationService.getCurrentUser();
        NotificationTemplate template = notificationTemplateRepository.findByIdAndCreatedById(id,user.getId())
                        .orElseThrow(() -> new IllegalStateException("Notification template not found"));
        template.setSubject(templateRequest.getSubject());
        template.setBody(templateRequest.getBody());
        template.setActive(true);
        NotificationTemplate savedTemplate = notificationTemplateRepository.save(template);
        return notificationMapper.toTemplateResponse(savedTemplate);
    }



    @Transactional
    public TemplateResponse findNotificationTemplateById(Long id) {
        User user = authenticationService.getCurrentUser();
       NotificationTemplate template =   notificationTemplateRepository.findByIdAndCreatedById(id,user.getId())
                .orElseThrow(() ->  new IllegalStateException("Notification template not found"));
       return notificationMapper.toTemplateResponse(template);
    }


    @Transactional
    public List<TemplateResponse> findAllNotificationTemplates() {
        User user = authenticationService.getCurrentUser();
        List<NotificationTemplate> list = notificationTemplateRepository.findByCreatedById(user.getId());
        return list.stream().map(notificationMapper::toTemplateResponse).toList();
    }


    @Transactional
    public void deleteNotificationTemplateById(Long id) {
        User user = authenticationService.getCurrentUser();
        notificationTemplateRepository.deleteByIdAndCreatedById(id,user.getId());
    }


}
