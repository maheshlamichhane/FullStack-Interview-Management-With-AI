package com.itsutra.project.advice;



import com.itsutra.project.exception.InterviewException;
import com.itsutra.project.exception.SlotNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
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


//    @ExceptionHandler(InsufficientSharesException.class)
//    public ProblemDetail handleException(InsufficientSharesException ex) {
//        return build(HttpStatus.BAD_REQUEST, ex, problem -> {
//            problem.setType(URI.create("http://example.com/problems/insufficient-shares"));
//            problem.setTitle("Insufficient Shares");
//        });
//    }

    private ProblemDetail build(HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        consumer.accept(problem);
        return problem;
    }

}
