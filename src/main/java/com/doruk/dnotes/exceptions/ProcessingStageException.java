package com.doruk.dnotes.exceptions;

public class ProcessingStageException extends RuntimeException {
    public ProcessingStageException(String message) {
        super(message);
    }

    public ProcessingStageException(String message, Exception cause) {
        super(message, cause);
    }
}
