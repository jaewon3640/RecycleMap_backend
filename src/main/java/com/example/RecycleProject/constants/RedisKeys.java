package com.example.RecycleProject.constants;

/**
 * Redis 키 접두사를 한 곳에 모은 상수 클래스.
 * <p>
 * 기존에는 "refresh:", "blacklist:" 문자열이 LoginController/JwtAuthenticationFilter 등
 * 여러 파일에 흩어져 있어 오타가 나도 컴파일 에러 없이 런타임에 조용히 깨질 위험이 있었다.
 * 키 생성을 헬퍼 메서드로 통일해 그 위험을 제거한다.
 */
public final class RedisKeys {

    private RedisKeys() {
        // 상수 전용 클래스 - 인스턴스화 방지
    }

    /** refresh:{userId} = refreshToken (TTL 14d) */
    public static final String REFRESH_PREFIX = "refresh:";

    /** blacklist:{accessToken} = "logout" (TTL = 남은 만료초) */
    public static final String BLACKLIST_PREFIX = "blacklist:";

    /** refresh_lock:{userId} = "1" (TTL 짧게) - 동시 refresh 중복 방지(SETNX) */
    public static final String REFRESH_LOCK_PREFIX = "refresh_lock:";

    public static String refresh(Long userId) {
        return REFRESH_PREFIX + userId;
    }

    public static String blacklist(String accessToken) {
        return BLACKLIST_PREFIX + accessToken;
    }

    public static String refreshLock(Long userId) {
        return REFRESH_LOCK_PREFIX + userId;
    }
}
