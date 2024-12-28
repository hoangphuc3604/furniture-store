package com.furnistyle.furniturebackend.exceptions;

public class BadRequestException extends RuntimeException {
    final String message;

    public BadRequestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
