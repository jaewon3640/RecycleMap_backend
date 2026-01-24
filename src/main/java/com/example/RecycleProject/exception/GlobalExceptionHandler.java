package com.example.RecycleProject.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 우리가 만든 BusinessException과 그 자식들(RegionNotFound 등)을 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException e) {
        // 400 Bad Request 상태코드와 함께 에러 메시지 반환
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // 그 외 예상치 못한 모든 에러(500 에러 등)를 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception e) {
        return ResponseEntity.internalServerError().body("서버 내부 오류가 발생했습니다.");
    }
    
}
