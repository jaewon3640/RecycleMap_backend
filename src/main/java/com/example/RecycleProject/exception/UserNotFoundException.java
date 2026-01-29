package com.example.RecycleProject.exception;

public class UserNotFoundException extends BusinessException{
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
