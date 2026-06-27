package com.example.RecycleProject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(cf);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    // 캐시 이름 — @Cacheable / @CacheEvict 의 value 와 반드시 일치시킨다.
    public static final String CACHE_TRASH_ONE = "trashDetailOne"; // 배출요령 단건
    public static final String CACHE_TRASH_ALL = "trashDetailAll"; // 배출요령 카테고리 전체
    public static final String CACHE_SCHEDULE  = "schedule";       // 배출일정

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory cf) {
        // 직렬화 등 공통 베이스 (TTL은 캐시별로 덮어쓴다)
        RedisCacheConfiguration base = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
                );

        // [캐시별 차등 TTL] 데이터 변동성에 맞춰 기준 TTL을 다르게 두고,
        //   각 TTL에 랜덤 지터를 더해 키들이 '동시에' 만료되는 Cache Stampede(thundering herd)를 방지한다.
        Map<String, RedisCacheConfiguration> caches = Map.of(
                // 배출요령 단건: 사실상 불변 → 길게 (2시간 ± 0~20분)
                CACHE_TRASH_ONE, base.entryTtl(randomizedTtl(Duration.ofHours(2), Duration.ofMinutes(20))),
                // 배출요령 전체: 품목 추가 시 변동 여지 있어 약간 짧게 (1시간 ± 0~10분)
                CACHE_TRASH_ALL, base.entryTtl(randomizedTtl(Duration.ofHours(1), Duration.ofMinutes(10))),
                // 배출일정: 등록(@CacheEvict)으로 갱신되므로 가장 짧게 (10분 ± 0~2분)
                CACHE_SCHEDULE,  base.entryTtl(randomizedTtl(Duration.ofMinutes(10), Duration.ofMinutes(2)))
        );

        return RedisCacheManager.builder(cf)
                // 위 목록에 없는 캐시는 기본 30분 ± 0~5분
                .cacheDefaults(base.entryTtl(randomizedTtl(Duration.ofMinutes(30), Duration.ofMinutes(5))))
                .withInitialCacheConfigurations(caches)
                // [모니터링] 캐시별 hit/miss 통계 수집 활성화(기본 OFF).
                //   Micrometer가 cache_gets_total{result=hit|miss} 메트릭으로 노출 → Grafana 캐시 히트율 패널.
                .enableStatistics()
                .build();
    }

    /**
     * base TTL + [0, maxJitter] 사이의 랜덤 오프셋을 반환하는 TtlFunction.
     * 캐시 엔트리마다 만료 시각이 흩어져 동시 만료(Cache Stampede)를 회피한다.
     */
    private RedisCacheWriter.TtlFunction randomizedTtl(Duration base, Duration maxJitter) {
        return (key, value) -> {
            long extraSeconds = ThreadLocalRandom.current().nextLong(maxJitter.toSeconds() + 1);
            return base.plusSeconds(extraSeconds);
        };
    }
}
