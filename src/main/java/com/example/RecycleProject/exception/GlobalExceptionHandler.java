package com.example.RecycleProject.exception;


import com.example.RecycleProject.DTO.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
// 모든 컨트롤러의 예외를 여기서 잡는다.
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 우리가 만든 BusinessException과 그 자식들(RegionNotFound 등)을 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        // 400 Bad Request 상태코드와 함께 에러 메시지 반환
        log.warn("[Exception] RegionNotFound: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse("BUSINESS_ERROR", e.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    // 그 외 예상치 못한 모든 에러(500 에러 등)를 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception e) {
        log.error("[Exception] Unexpected Error: ", e); // 이건 에러 레벨로 상세히!
        ErrorResponse response = new ErrorResponse("SERVER_ERROR", "서버 내부 오류가 발생했습니다.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}

