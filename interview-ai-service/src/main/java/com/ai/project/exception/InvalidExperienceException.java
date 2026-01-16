package com.ai.project.exception;

public class InvalidExperienceException extends RuntimeException {
    private static final String MESSAGE = "Experience must be greater than 3";
    public InvalidExperienceException() {
        super(MESSAGE);
    }
}
