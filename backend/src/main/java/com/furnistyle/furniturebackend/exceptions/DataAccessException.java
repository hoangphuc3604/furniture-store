package com.furnistyle.furniturebackend.exceptions;

public class DataAccessException extends RuntimeException {
    final String message;

    public DataAccessException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
