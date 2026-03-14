package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.JoinRequest;
import com.example.RecycleProject.DTO.JwtDTO;
import com.example.RecycleProject.DTO.LoginRequest;
import com.example.RecycleProject.JWT.JwtTokenProvider;
import com.example.RecycleProject.domain.User;
import com.example.RecycleProject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

import static com.example.RecycleProject.DTO.JwtDTO.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class LoginController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid JoinRequest request) {
        // 비밀번호 확인 로직

        if (!request.getPassword().equals(request.getPasswordCheck())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        userService.join(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    /*
        로그인 성공시에 토큰을 발급 받을수 있도록한다.
        1. 유저 검증
        2. 토큰생성
     */

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request){

        User user = userService.login(request);

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(),user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(),user.getRole());

        redisTemplate.opsForValue().set(
                "refresh:" + user.getId(),
                refreshToken,
                Duration.ofDays(14)
        );

        TokenResponse response = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(user.getName())
                .role(user.getRole().name())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal Long userId,
                                         jakarta.servlet.http.HttpServletRequest request) {
        // 1. Access Token 추출 (null 방어)
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String accessToken = bearerToken.substring(7);

            // 2. Access Token 남은 만료시간만큼 블랙리스트 등록
            //    만료된 토큰이면 ExpiredJwtException이 발생할 수 있으므로 try-catch 처리
            try {
                long remainingSeconds = jwtTokenProvider.getRemainingSeconds(accessToken);
                if (remainingSeconds > 0) {
                    redisTemplate.opsForValue().set(
                            "blacklist:" + accessToken,
                            "logout",
                            Duration.ofSeconds(remainingSeconds)
                    );
                }
            } catch (Exception e) {
                // 토큰이 이미 만료되었거나 유효하지 않으면 블랙리스트 등록 불필요
            }
        }

        // 3. Refresh Token 삭제 - userId가 null이면 토큰에서 직접 추출
        Long resolvedUserId = userId;
        if (resolvedUserId == null && bearerToken != null && bearerToken.startsWith("Bearer ")) {
            try {
                resolvedUserId = jwtTokenProvider.getUserId(bearerToken.substring(7));
            } catch (Exception e) {
                // 토큰에서 userId 추출 실패 시 무시
            }
        }
        if (resolvedUserId != null) {
            redisTemplate.delete("refresh:" + resolvedUserId);
        }

        return ResponseEntity.ok("로그아웃 성공");
    }

    @GetMapping("/me")
    public ResponseEntity<TokenResponse.UserResponse> me(@AuthenticationPrincipal Long userId) {
        User user = userService.findOne(userId);

        // UserResponse DTO로 변환하여 민감 정보(비밀번호 등) 제외
        TokenResponse.UserResponse response = TokenResponse.UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getName())
                .role(user.getRole().name())
                .build();

        return ResponseEntity.ok(response);
    }
}
