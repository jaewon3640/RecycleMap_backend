package com.example.RecycleProject.exception;

public class BoardNotFoundException extends BusinessException {
    public BoardNotFoundException(String message) {
        super(message);
    }

    public BoardNotFoundException() {
        super();
    }

    public BoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoardNotFoundException(Throwable cause) {
        super(cause);
    }
}
