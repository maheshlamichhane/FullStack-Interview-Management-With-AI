package com.itsutra.project.controller;

import com.itsutra.project.common.service.AuthenticationService;
import com.itsutra.project.dto.TemplateRequest;
import com.itsutra.project.dto.TemplateResponse;
import com.itsutra.project.service.NotificationTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class TemplateController {

    private final NotificationTemplateService notificationTemplateService;
    private final AuthenticationService authenticationService;

    @PostMapping
    public TemplateResponse createTemplate(
            @Valid @RequestBody TemplateRequest request) {
        return notificationTemplateService.createNotificationTemplate(request);
    }


    @PutMapping("/{id}")
    public TemplateResponse updateTemplate(@PathVariable Long id, @Valid @RequestBody TemplateRequest request) {
        return notificationTemplateService.updateNotificationTemplate(request,id);
    }

    @GetMapping("/{id}")
    public TemplateResponse findTemplateById(@PathVariable Long id) {
        return notificationTemplateService.findNotificationTemplateById(id);
    }

    @GetMapping
    public List<TemplateResponse> findAllNotificationTemplates() {
        return notificationTemplateService.findAllNotificationTemplates();
    }

    @DeleteMapping("/{id}")
    public void deleteNotificationTempateById(@PathVariable Long id) {
        notificationTemplateService.deleteNotificationTemplateById(id);
    }

}
