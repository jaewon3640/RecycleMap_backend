package com.example.RecycleProject.exception;

public class ReplyNotFoundException extends BusinessException {
    public ReplyNotFoundException(String message) {
        super(message);
    }

    public ReplyNotFoundException() {
        super();
    }

    public ReplyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReplyNotFoundException(Throwable cause) {
        super(cause);
    }
}
