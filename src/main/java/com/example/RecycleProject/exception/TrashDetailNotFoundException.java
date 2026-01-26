package com.example.RecycleProject.exception;

public class TrashDetailNotFoundException extends BusinessException{
    public TrashDetailNotFoundException(String message) {
        super(message);
    }

    public TrashDetailNotFoundException() {
        super();
    }

    public TrashDetailNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrashDetailNotFoundException(Throwable cause) {
        super(cause);
    }
}
