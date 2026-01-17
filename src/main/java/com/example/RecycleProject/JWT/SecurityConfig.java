package com.example.RecycleProject.JWT;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

//@EnableWebSecurity Spring Security 기능을 켜는 어노테이션
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    /*
        SecurityFilterChain 객체를 Spring 컨테이너에 등록한다.
        이제 Spring Security가 내가 정의한 이 규칙을 사용한다.
        -csrf.disable() : CSRF를 꺼주는 메서드, CSRF는 주로 “세션 기반 + 브라우저 쿠키 인증”에서
        문제되는 공격이므로 JWT token에서는 문제가 없음

        -sessionManagement STATELESS 세션을 만들거나 사용하지 말아라, token으로 하므로

        -authorizeHttpRequests 인가 설정 : 누가 어떤 URL에 접근이 가능한가? 유저기능과 관리자 기능을 나누기
        -addFilterBefore : 필터가 실행되기 전에 인증정보를 넣어두는것
        -http.build() 최종 빌드하여주기
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // REST API이므로 CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // ADMIN 권한만 가능
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN") // 둘 다 가능
                        .anyRequest().authenticated()
                )
                // UsernamePasswordAuthenticationFilter 이전에 JwtAuthenticationFilter 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}