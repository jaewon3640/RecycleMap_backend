package com.example.RecycleProject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Redis 캐시 전용 List 래퍼.
 *
 * <p>{@code GenericJackson2JsonRedisSerializer} 는 <b>root-level 컬렉션</b>을 round-trip 하지 못한다.
 * write 시 타입 래퍼 없는 bare 배열({@code [{"@class":...}, ...]})로 저장되는데,
 * read 시에는 컬렉션의 타입 정보를 기대하므로 {@code AsArrayTypeDeserializer} 가
 * 첫 토큰에서 타입 ID 문자열을 못 찾고 역직렬화에 실패한다(→ 캐시 hit 시 500).
 *
 * <p>List 를 객체의 필드로 한 번 감싸면 root 가 객체가 되어 {@code @class} 가 부여되고,
 * {@code items} 는 선언된 컬렉션 필드로서 각 요소의 {@code @class} 기반으로 정상 역직렬화된다.
 * 단건 객체 캐시는 이 문제가 없으므로 List 반환 캐시에만 사용한다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheList<T> implements Serializable {
    private List<T> items;
}
