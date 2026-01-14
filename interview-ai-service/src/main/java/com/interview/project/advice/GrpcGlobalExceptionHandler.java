package com.interview.project.advice;

import com.interview.project.exception.InvalidExperienceException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcGlobalExceptionHandler {

    @GrpcExceptionHandler(InvalidExperienceException.class)
    public Status handleInvalidArguments(InvalidExperienceException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage());
    }

}
