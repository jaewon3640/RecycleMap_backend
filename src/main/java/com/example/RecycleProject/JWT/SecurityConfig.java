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
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CORS 설정 연결
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. CSRF 비활성화 (JWT 사용 시 필수)
                .csrf(csrf -> csrf.disable())

                // 3. 세션 사용 안 함 (STATELESS 설정)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. 요청별 인가(Authorization) 설정
                // SecurityConfig.java
                // SecurityConfig.java 수정
                // SecurityConfig.java 수정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // ⭐ 구체적인 경로를 위로!
                        .requestMatchers("/api/user/region").authenticated()
                        .requestMatchers("/api/feedbacks/save").authenticated() // 다시 authenticated로 변경

                        // ⭐ 나머지 조회용은 아래로
                        .requestMatchers("/api/trash-detail/**", "/api/schedules/**", "/api/feedbacks/**").permitAll()

                        .anyRequest().authenticated()
                )
                // 5. JWT 인증 필터 추가 (UsernamePasswordAuthenticationFilter 보다 먼저 실행)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)

                // 6. H2 Console 등 프레임 사용을 위한 설정 (개발 시 필요)
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