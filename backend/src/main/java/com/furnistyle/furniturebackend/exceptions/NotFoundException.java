package com.furnistyle.furniturebackend.exceptions;

public class NotFoundException extends RuntimeException {
    final String message;

    public NotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
