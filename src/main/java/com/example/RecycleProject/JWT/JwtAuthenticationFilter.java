package com.example.RecycleProject.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 사용자의 한번의 요청에 대해 한번만 실행되도록 보장 자원낭비 감소

    private final JwtTokenProvider jwtTokenProvider; //JWT 생성/검증/파싱 담당 클래스

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();
        System.out.println("DEBUG Filter: 요청 경로 = " + path + ", 메서드 = " + method);
        String token = resolveToken(request);
        System.out.println("DEBUG Filter: 추출된 토큰 = " + token);

        // JwtAuthenticationFilter.java 내부 수정 제안
        // JwtAuthenticationFilter.java 내부 수정
        if (token != null && jwtTokenProvider.isValid(token)) {
            // 1. 토큰에서 Long 타입의 ID를 추출 (기존 getUserId 메서드 활용)
            Long userId = jwtTokenProvider.getUserId(token);

            // 2. 권한 설정 (ROLE_ 접두사 포함)
            List<SimpleGrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

            // 3. 첫 번째 인자(Principal)로 userId(Long)를 전달
            // 이 값이 컨트롤러의 @AuthenticationPrincipal Long userId로 들어갑니다.
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // Header에서 "Authorization": "Bearer {TOKEN}" 패턴을 찾아 토큰값만 추출
    //주어진 HttpServletRequest에서 토큰 정보를 추출하는 역할
    /*
     Authorization 헤더 읽기 -> Bearer 방식으로 저장하므로 Bearer 문자열을 제거후에 순수 JWT 문자열을 반환한다.
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}