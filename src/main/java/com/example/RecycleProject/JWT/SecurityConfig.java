package com.example.RecycleProject.JWT;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
    // SecurityConfig.java 수정
    // SecurityConfig.java의 filterChain 메서드 부분 수정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. OPTIONS (Preflight) 허용
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2. 게시판 관련 조회 경로를 가장 우선순위로 두어 permitAll 설정
                        // 이 설정이 .anyRequest().authenticated() 보다 반드시 위에 있어야 합니다.
                        .requestMatchers("/api/board/search-name", "/api/board/{id}").permitAll()
                        .requestMatchers("/api/board/**").permitAll()

                        // 3. 인증이 필요한 다른 경로들
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/user/region").authenticated()
                        .requestMatchers("/api/feedbacks/save").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    //테스트용
    /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // POST 테스트를 위해 CSRF 비활성화 (필수!)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청을 로그인 없이 허용
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())); // H2 Console 접근용

        return http.build();
    }
     */

    // SecurityConfig.java에 추가 권장
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // 프론트 포트
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}