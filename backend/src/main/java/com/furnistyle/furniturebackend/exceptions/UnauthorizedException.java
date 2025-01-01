package com.furnistyle.furniturebackend.exceptions;

public class UnauthorizedException extends RuntimeException {
    final String message;

    public UnauthorizedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}