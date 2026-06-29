package com.example.RecycleProject.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 토큰 재발급 요청 DTO.
 * 클라이언트가 localStorage에 보관하던 refreshToken 을 그대로 담아 보낸다.
 */
@Data
public class RefreshRequest {

    @NotBlank(message = "refreshToken 은 필수입니다.")
    private String refreshToken;
}
