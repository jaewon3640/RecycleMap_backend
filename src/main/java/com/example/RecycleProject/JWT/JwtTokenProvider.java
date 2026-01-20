package com.example.RecycleProject.JWT;

import com.example.RecycleProject.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key; // 대칭키
    private final long accessExpSeconds; // 만료시간
    private final long refreshExpSeconds; //refreshToken의 만료시간

    /*
     yaml 파일로 부터 값을 주입받고, hmacShaKeyFor 방식을 사용하여 키를 만들고
     이에 대한 만료 시간을 계산하여 준다.
     */
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-exp-min}") long accessMin,
            @Value("${jwt.refresh-exp-days}") long refreshDays
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpSeconds = accessMin * 60;
        this.refreshExpSeconds = refreshDays * 24 * 60 * 60;
    }

    /*
        공통로직부 이부분은 access token과 refresh token의 생성부
     */

    public String createAccessToken(Long userId, Role role) {
        return createToken(userId, role, accessExpSeconds);
    }

    public String createRefreshToken(Long userId, Role role) {
        return createToken(userId, role, refreshExpSeconds);
    }

    /*
        토큰생성 로직 : 발급시간, 만료시간을 정의한후에
        SUBJECT : 토큰의 주인은 누구,,? 즉 userId 식별자
        issuedAt : 토큰 발급시각
        expiration : 만료시간
        signWith : 서명(위조방지)
        compact : 문자열 JWT 토큰을 생성하라
     */

    // createToken 메서드에 Role 파라미터 추가 커스텀 클레임을 통해 Payload부분에 Role을 추가
    private String createToken(Long userId, Role role, long expSeconds) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role.name()) // "role": "USER" 형태로 저장
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(expSeconds)))
                .signWith(key)
                .compact();
    }

    /*
    토큰 유효성 검사 : 서명을 검증하고 만약 excetiion시에 예외를 터트려라
     */
    public boolean isValid(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | io.jsonwebtoken.MalformedJwtException e) {
            // 잘못된 서명 또는 잘못된 형식의 토큰
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // 만료된 토큰 -> 이때 '재발급'이 필요하다는 신호를 줘야 함
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            // 지원되지 않는 토큰
        } catch (IllegalArgumentException e) {
            // 토큰이 비어있거나 잘못됨
        }
        return false;
    }

    //JWT 문자열에서 userId를 꺼내서 Long으로 반환
    //verifyWith(key)를 통해서 JWT서명을 검증하여 준다.
    //build를 통해서 JwtParser 객체 생성, parseSignedClaims : 실제 검증 + 파실
    //PayLoad 부분만 추출해낸다. 이를 총해서 이를 Long 타입으로 변환
    public Long getUserId(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();

        return Long.parseLong(claims.getSubject());
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload().get("role", String.class);
    }
}
