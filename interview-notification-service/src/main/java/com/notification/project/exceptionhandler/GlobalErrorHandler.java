package com.notification.project.exceptionhandler;

import com.notification.project.exception.NotificationTemplateNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);

        HttpStatus status;
        String message;

        if (ex instanceof NotificationTemplateNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = ex.getMessage();
        } else if (ex instanceof ConstraintViolationException violationEx) {
            status = HttpStatus.BAD_REQUEST;
            // Join all validation messages into one
            message = violationEx.getConstraintViolations()
                    .stream()
                    .map(cv -> cv.getPropertyPath() + " " + cv.getMessage())
                    .reduce((m1, m2) -> m1 + "; " + m2)
                    .orElse("Validation failed");
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Internal server error";
        }

        // Build JSON response
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", Instant.now().toString());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        errorResponse.put("path", exchange.getRequest().getPath().value());

        // Convert to JSON bytes
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        byte[] bytes = errorResponse.toString().getBytes(StandardCharsets.UTF_8);
        var buffer = bufferFactory.wrap(bytes);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}

