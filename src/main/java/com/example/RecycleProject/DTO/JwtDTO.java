package com.example.RecycleProject.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class JwtDTO {
    // 로그인 응답 DTO
    @Data
    @Builder
    @AllArgsConstructor
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
        private String nickname;
        private String role; // 👈 이 줄을 추가하세요!    }

        // 내 정보 응답 DTO (비밀번호 제외)
        @Data
        @Builder
        @AllArgsConstructor
        public static class UserResponse {
            private Long id;
            private String email;
            private String nickname;
            private String role;
        }
    }
}
