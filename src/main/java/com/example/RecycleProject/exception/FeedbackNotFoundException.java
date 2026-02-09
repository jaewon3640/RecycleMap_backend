package com.example.RecycleProject.exception;

public class FeedbackNotFoundException extends BusinessException{
    public FeedbackNotFoundException(String message) {
        super(message);
    }

    public FeedbackNotFoundException() {
        super();
    }

    public FeedbackNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeedbackNotFoundException(Throwable cause) {
        super(cause);
    }
}
