package com.doruk.dnotes.exceptions;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Exception cause) {
        super(message, cause);
    }
}
