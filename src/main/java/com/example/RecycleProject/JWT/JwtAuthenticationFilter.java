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

        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.isValid(token)) {
            Long userId = jwtTokenProvider.getUserId(token);
            String role = jwtTokenProvider.getRole(token); // 토큰에서 역할 추출

            // SimpleGrantedAuthority를 사용하여 권한 부여
            // 즉 권한 객체를 생성하여주고(역할에 따라)
            List<SimpleGrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

            //Authrntication 객체 또한 생성해주는 코드
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);


            //성한 "신분증"을 보안 보관함에 넣는다. 이 작업이 완료되어야 컨트롤러에서 "로그인 된 사용자"로 인식가능
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