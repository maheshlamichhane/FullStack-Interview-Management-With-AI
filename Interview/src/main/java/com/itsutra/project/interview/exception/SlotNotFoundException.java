package com.itsutra.project.interview.exception;

public class SlotNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Slot [id=%d] is not found";

    public SlotNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }

}
