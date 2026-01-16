package com.notification.project.router;

import com.notification.project.handler.NotificationTemplateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class NotificationTemplateRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(NotificationTemplateHandler handler) {
        return route(POST("/api/notifications"), handler::createTemplate)
                .andRoute(PUT("/api/notifications/{id}"), handler::updateTemplate)
                .andRoute(GET("/api/notifications/{id}"), handler::getTemplateById)
                .andRoute(GET("/api/notifications"), handler::getAllTemplates)
                .andRoute(DELETE("/api/notifications/{id}"), handler::deleteTemplate)
                .andRoute(POST("/api/notifications/send"), handler::sendNotification);
    }


}


