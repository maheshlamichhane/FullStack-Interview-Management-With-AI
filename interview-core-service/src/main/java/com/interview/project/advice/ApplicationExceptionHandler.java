package com.interview.project.advice;


import com.interview.project.interview.exception.InterviewException;
import com.interview.project.interview.exception.SlotNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(SlotNotFoundException.class)
    public ProblemDetail handleException(SlotNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex, problem -> {
            problem.setType(URI.create("http://example.com/problems/slot-not-found"));
            problem.setTitle("Slot Not Found");
        });
    }


    @ExceptionHandler(InterviewException.class)
    public ProblemDetail handleException(InterviewException ex) {
        return build(HttpStatus.BAD_REQUEST, ex, problem -> {
            problem.setType(URI.create("http://example.com/problems/sot-problem"));
            problem.setTitle("Slot problem");
        });
    }


    @ExceptionHandler(WebExchangeBindException.class)
    public ProblemDetail handleValidationException(
            WebExchangeBindException ex,
            ServerWebExchange exchange
    ) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setType(URI.create("https://example.com/problems/validation-error"));
        problem.setTitle("Validation Failed");
        problem.setDetail("Request validation failed");
        problem.setInstance(URI.create(exchange.getRequest().getPath().value()));

        // Add field errors as an extension property (RFC-compliant)
        List<Map<String, String>> errors = ex.getFieldErrors()
                .stream()
                .map(err -> Map.of(
                        "field", err.getField(),
                        "message", err.getDefaultMessage()
                ))
                .toList();

        problem.setProperty("errors", errors);
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }


    private ProblemDetail build(HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        consumer.accept(problem);
        return problem;
    }


}
