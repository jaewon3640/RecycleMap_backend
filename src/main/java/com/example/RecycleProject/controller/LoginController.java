package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.JoinRequest;
import com.example.RecycleProject.DTO.JwtDTO;
import com.example.RecycleProject.DTO.LoginRequest;
import com.example.RecycleProject.JWT.JwtTokenProvider;
import com.example.RecycleProject.domain.User;
import com.example.RecycleProject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.example.RecycleProject.DTO.JwtDTO.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class LoginController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;


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

        TokenResponse response = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(user.getName())
                .role(user.getRole().name())
                .build();

        return ResponseEntity.ok(response);
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
