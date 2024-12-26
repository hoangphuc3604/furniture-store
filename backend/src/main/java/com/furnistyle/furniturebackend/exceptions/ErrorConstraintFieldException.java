package com.furnistyle.furniturebackend.exceptions;

public class ErrorConstraintFieldException extends RuntimeException {
    String message;

    public ErrorConstraintFieldException(String message) {
        this.message = message;
    }

    public ErrorConstraintFieldException(String message, String message1) {
        super(message);
        this.message = message1;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
