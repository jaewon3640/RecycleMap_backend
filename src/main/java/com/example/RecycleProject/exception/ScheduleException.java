package com.example.RecycleProject.exception;

// 일정이 중복되거나 없을 때
public class ScheduleException extends BusinessException {
    public ScheduleException(String message) {
        super(message);
    }

    public ScheduleException() {
    }

    public ScheduleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScheduleException(Throwable cause) {
        super(cause);
    }

}
