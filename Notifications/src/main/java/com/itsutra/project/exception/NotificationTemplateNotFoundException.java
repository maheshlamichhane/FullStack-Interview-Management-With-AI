package com.itsutra.project.exception;

public class NotificationTemplateNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Notification Template [id=%d] is not found";

    public NotificationTemplateNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }

}
