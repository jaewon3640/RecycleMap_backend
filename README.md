# RecycleMap — 지역별 재활용 정보 제공 서비스

> 2025-2026 겨울방학 개인 프로젝트  
> Backend: Spring Boot 3.5 · Frontend: React 18 + Vite · 인프라: Docker · AWS EC2 · CI/CD: GitHub Actions

---

## 1. 프로젝트 개요

| 항목 | 내용 |
|------|------|
| 목적 | 지역마다 다른 재활용 배출 요령·일정을 한 곳에서 조회 |
| 개발 기간 | 2025년 겨울방학 |
| 리팩토링 기간 | 2026.06.19~ |
| 형태 | 개인 풀스택 프로젝트 |
| GitHub | [RecycleMap_backend](https://github.com/jaewon3640/RecycleMap_backend) · [RecycleMap_frontend](https://github.com/jaewon3640/RecycleMap_frontend) |

---

## 2. 기술 스택

### Backend
- **Java 17 · Spring Boot 3.5**
- **Spring Security** — JWT 기반 Stateless 인증 / 인가
- **Spring Data JPA** — MySQL 8 연동
- **Spring Data Redis** — Refresh Token 저장, 조회 캐싱 (TTL 30분), Access Token 블랙리스트
- **Spring AOP** — Controller / Service 계층 공통 실행 로그 (ThreadLocal 기반 traceId)
- **SpringDoc OpenAPI 2.8** — Swagger UI 자동 문서화
- **jjwt 0.12.5** — Access Token (15분) / Refresh Token (14일)

### Frontend
- **React 18 · TypeScript · Vite**
- **React Router v7** — SPA 라우팅
- **Tailwind CSS · Radix UI · shadcn/ui** — 컴포넌트 UI
- **Axios** — API 통신 · Proxy 설정

### 인프라 / DevOps
- **Docker + Docker Compose** — MySQL · Redis · Spring Boot · Nginx(React) 4-컨테이너 구성
- **AWS EC2** — 서버 배포
- **GitHub Actions** — push-to-main 시 자동 빌드 → Docker Hub Push → EC2 SSH 배포
- **환경변수 분리** — `.env` + GitHub Secrets로 민감정보 격리

---

## 3. 시스템 아키텍처

```
[Browser]
    │  HTTP (port 80)
    ▼
[Nginx (React 빌드 서빙)]
    │  API 요청 → http://app:8080
    ▼
[Spring Boot App]  ←── JWT 인증 필터 ──── [Redis]
    │                                         │
    │  JPA                               · Refresh Token
    ▼                                    · Access Token 블랙리스트
[MySQL 8]                                · 조회 캐시
```

**CI/CD 흐름**

```
git push (main)
    ↓
GitHub Actions
  ① JDK 17 세팅
  ② MySQL·Redis 서비스 컨테이너 기동 (헬스체크 통과 후)
  ③ ./gradlew clean build (통합 테스트 포함)
  ④ Docker 이미지 빌드 → Docker Hub Push
  ↓
EC2 SSH 접속
  ⑤ docker pull latest
  ⑥ docker-compose.prod.yml up --no-deps app
```

---

## 4. 핵심 기능 및 구현 포인트

### 4-1. JWT 인증 / 인가

- **Access Token** 은 짧은 유효기간(15분), **Refresh Token** 은 Redis에 14일간 저장 (`refresh:{userId}` 키)
- 로그아웃 시 Access Token의 남은 TTL 동안 Redis 블랙리스트(`blacklist:{token}`)에 등록 → 재사용 차단
- `JwtAuthenticationFilter`에서 매 요청마다 블랙리스트 조회 → 인증 여부 판단
- `@AuthenticationPrincipal Long userId` — Spring Security Context에서 userId 주입

```java
// 로그아웃 핵심 로직 (LoginController.java)
long remainingSeconds = jwtTokenProvider.getRemainingSeconds(accessToken);
if (remainingSeconds > 0) {
    redisTemplate.opsForValue().set(
        "blacklist:" + accessToken, "logout",
        Duration.ofSeconds(remainingSeconds)
    );
}
redisTemplate.delete("refresh:" + resolvedUserId);
```

### 4-2. Redis 캐싱 (조회 성능)

- `@EnableCaching` + `RedisCacheManager` — TTL 30분, `GenericJackson2JsonRedisSerializer` 직렬화
- 자주 조회되는 배출 요령·지역 정보에 `@Cacheable` 적용
- **JMeter 부하 테스트 결과** (600 request): 평균 157ms · 최소 6ms · 오류율 2.7%

### 4-3. Spring AOP 실행 로그

- `@Aspect` + `@Around` — Controller·Service 전 계층 메서드에 자동 적용
- `ThreadLocal<TraceId>` 로 요청 1건의 흐름을 UUID로 묶어 출력 (중첩 호출 레벨 표시)
- 핵심 비즈니스 코드 수정 없이 로깅 횡단관심사 분리

### 4-4. 도메인 설계

| 엔티티 | 역할 |
|--------|------|
| User | 회원 정보, Role(USER/ADMIN), Region 연관 |
| Region | 지역 정보 |
| TrashDetail | 재활용 품목별 배출 요령 |
| DisposalSchedule | 지역별 배출 일정 |
| Board / BoardReply | 사용자 Q&A 게시판 |
| Feedback / FeedbackReply | 관리자 문의 피드백 |

- **역할 분리**: 일반 사용자(게시판 CRUD) vs 관리자(답변, 피드백 관리)
- 연관관계 편의 메서드 및 `@PrePersist` / `@PreUpdate`로 감사 필드 자동 처리

### 4-5. 컨테이너화 & 보안

- 4개 서비스(db · redis · app · frontend)를 `docker-compose.yml` 단일 파일로 오케스트레이션
- `healthcheck` 통과 후 `depends_on condition: service_healthy` 로 순서 보장
- 프로덕션용 `docker-compose.prod.yml` 은 Docker Hub 이미지 Pull 방식으로 EC2 빌드 제거
- `.env` + `.gitignore` 조합으로 비밀값 커밋 방지

---

## 5. 성능 최적화 — 측정 기반 Deep 분석

> 게시판(Board) 조회 경로를 대상으로 **문제 → 측정 → 원인 → 해결 → 검증** 흐름을 따라 4가지 최적화를 적용했다.
> 모든 측정은 로컬이 아니라 **AWS EC2 프로덕션 컨테이너**에서, 충분한 데이터 규모(board 10,030건)로 수행해 옵티마이저가 실제로 인덱스를 선택하는 조건을 만들었다.
> *(데이터가 적으면(수 건) 옵티마이저가 인덱스를 무시(`type: ALL`)하고 풀스캔을 택하므로 EXPLAIN 자체가 무의미하다 — 이 점을 먼저 확인하고 대량 데이터로 측정했다.)*

### 5-1. N+1 쿼리 — 목록 조회 시 연관 User 반복 조회

| 단계 | 내용 |
|------|------|
| **문제** | 게시판 목록 1페이지(30건) 조회 시 응답이 느리고 DB 쿼리가 비정상적으로 많음 |
| **측정** | EC2 컨테이너 로그(`org.hibernate.SQL: debug`)에서 실제 발생 쿼리 카운트<br>**Before: 30쿼리** — board 목록 1건 + 각 게시글 작성자 `SELECT … FROM user WHERE user_id=?` 29건 |
| **원인** | `Board → User` 연관이 `@ManyToOne(fetch = LAZY)` → 목록 순회 중 `getUser()` 접근마다 프록시 초기화 쿼리 발생 (전형적 N+1) |
| **해결** | `BoardRepository`에 **`JOIN FETCH b.user`** 적용, `countQuery`는 fetch 없이 분리 |
| **검증** | **After: 2쿼리** — board⋈user 단일 조인 쿼리 1 + count 1. user 개별 SELECT **0건** |

**왜 `JOIN FETCH`인가 (vs `default_batch_fetch_size`)** — `Board → User`는 **ToOne** 관계라 JOIN FETCH가 단일 쿼리로 끝나고, ToOne은 페이징에 영향이 없어 `countQuery` 분리만으로 정상 동작한다.
반면 JOIN FETCH를 컬렉션(`@OneToMany`) + 페이징에 쓰면 `HHH000104`(메모리에서 페이징) 경고가 뜬다. 그래서 **ToOne = JOIN FETCH, ToMany(페이징) = `default_batch_fetch_size`**로 역할을 분담했다.

```java
// BoardRepository.java
@Query(value = "SELECT b FROM Board b JOIN FETCH b.user ORDER BY b.createdAt DESC",
       countQuery = "SELECT COUNT(b) FROM Board b")
Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);
```

---

### 5-2. 인덱스 설계 — 정렬 풀스캔 + filesort 제거

| 단계 | 내용 |
|------|------|
| **문제** | `ORDER BY created_at DESC LIMIT 10` 목록 쿼리가 데이터 증가에 따라 선형으로 느려짐 |
| **측정** | EC2 board 10,030건, `EXPLAIN ANALYZE`(warm) 2회차 기준<br>**Before(인덱스 DROP): 5.83ms** — Table scan 10,030행 + Sort(filesort)<br>**After(인덱스 ADD): 0.05ms** — Index scan 10행, filesort 제거 |
| **원인** | 인덱스 부재 시 전체 행을 읽어 메모리/디스크에서 정렬(filesort) → 읽는 행이 데이터 수에 비례(O(n)) |
| **해결** | `Board` 엔티티에 정렬 컬럼 기준 복합 인덱스 설계<br>`idx_board_created(created_at DESC)`, `idx_board_user_created(user_id, created_at DESC)` |
| **검증** | EXPLAIN: 전체목록 Q `type:index, key:idx_board_created, Extra:NULL`(filesort 없음) / 유저별 Q `type:ref, key:idx_board_user_created` |

**핵심은 단순 "117배 빠름"이 아니라 확장성** — Before는 O(n)으로 데이터가 늘수록 선형 증가하지만, After는 인덱스 정렬 순서를 그대로 따라가 LIMIT 행만 읽으므로 **데이터 규모와 무관하게 거의 일정(O(log n))**하다.

**왜 복합 인덱스인가** — 유저별 조회(`WHERE user_id=? ORDER BY created_at DESC`)에서 단일 FK 인덱스만 있으면 user_id로 필터 후 정렬(filesort)이 또 필요하다. `(user_id, created_at DESC)` 복합 인덱스는 **필터 + 정렬을 한 번에** 해결해 `Extra:NULL`을 만든다. EXPLAIN의 `possible_keys`에 FK 인덱스도 떴지만 옵티마이저가 복합 인덱스를 선택한 것이 그 증거다.

---

### 5-3. LIKE 검색의 인덱스 한계 규명 (해결이 아닌 "한계 정의")

| 단계 | 내용 |
|------|------|
| **문제** | 제목 검색 `WHERE title LIKE '%키워드%'`는 인덱스로 최적화되지 않음 |
| **측정** | EXPLAIN(10k): **`possible_keys: NULL`** — title 인덱스를 후보로조차 올리지 못함. `Using where`로 행마다 title 비교(`filtered: 11.11`) |
| **원인** | B-Tree 인덱스는 **좌측 접두사 매칭**만 가능 → 앞 와일드카드(`%키워드`)는 시작점을 특정할 수 없어 인덱스 사용 불가 |
| **(현재 상태)** | `type`이 `ALL`이 아닌 건 `ORDER BY created_at + LIMIT` 덕에 정렬 인덱스로 우회한 것뿐, **검색 조건 자체는 풀스캔(행마다 비교)**임을 정확히 규명 |
| **해결 방향** | 데이터/트래픽 증가 시 ① MySQL **Full-Text Index(`MATCH … AGAINST`)** 도입, ② 규모가 더 커지면 **ElasticSearch** 분리 |

> 모든 문제를 코드로 "해결"하는 대신, **인덱스가 통하지 않는 경계를 데이터로 증명하고 적정 기술(검색 엔진)을 제시**한 케이스. 무조건 인덱스를 추가하는 대신 한계를 아는 것이 설계 역량이라고 판단했다.

---

### 5-4. 커서 기반 페이징 — OFFSET deep paging 해결

| 단계 | 내용 |
|------|------|
| **문제** | OFFSET 페이징은 뒤 페이지로 갈수록 느려짐(deep paging) |
| **측정** | EC2 10k, `EXPLAIN ANALYZE`(warm)<br>**OFFSET 1페이지(OFFSET 0): 0.038ms / 10행**<br>**OFFSET 1000페이지(OFFSET 10000): 5.06ms / 10,010행** — 1만 행을 읽고 버림<br>**커서(`board_id < lastId`): 0.03ms / 10행** — 위치 무관 일정 |
| **원인** | OFFSET은 건너뛸 행도 **모두 읽은 뒤 버린다** → 읽는 행 = OFFSET + LIMIT(위치에 비례) |
| **해결** | PK 기준 커서 페이징 — `WHERE b.id < :lastId ORDER BY b.id DESC LIMIT n` 으로 인덱스를 직접 seek (`+ JOIN FETCH`로 N+1도 방지) |
| **검증** | 읽는 행 = LIMIT(일정). 1페이지 0.038ms → 1000페이지 5.06ms는 **133배** 차이, 커서 깊은 페이지(0.03ms) vs OFFSET 깊은 페이지(5.06ms)는 **약 170배**. 커서 깊은 페이지 ≈ OFFSET 1페이지 → "위치 무관 일정" 입증 |

**trade-off까지 명시** — 커서 페이징은 임의 페이지 점프·총 페이지 수 계산이 불가능해 **무한 스크롤 UI**에 적합하고, 번호 페이징 UI에는 OFFSET이 맞다. 그래서 둘을 **공존**시켜(`GET /api/board` = OFFSET, `GET /api/board/cursor` = 커서) 상황에 맞게 쓰도록 설계했다.

```java
// BoardRepository.java
@Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.id < :lastId ORDER BY b.id DESC")
List<Board> findByIdLessThanOrderByIdDesc(@Param("lastId") Long lastId, Pageable pageable);
```

---

### 성능 최적화 요약

| 영역 | Before | After | 핵심 포인트 |
|------|--------|-------|------------|
| N+1 | 30 쿼리 | **2 쿼리** | ToOne=JOIN FETCH, ToMany=batch 역할 분담 |
| 인덱스 정렬 | 5.83ms (10,030행 scan + filesort) | **0.05ms** (10행 index scan) | O(n)→O(log n) 확장성, 복합 인덱스로 filesort 제거 |
| LIKE 검색 | possible_keys NULL (풀스캔) | (한계 규명) | B-Tree 좌측 접두사 한계 → Full-Text/ES 제시 |
| 페이징 | OFFSET 1000p 5.06ms / 10,010행 | **커서 0.03ms / 10행** | deep paging 해결, OFFSET과 공존 설계 |

---

## 6. 트러블슈팅

### 6-1. JWT 401/403 혼재 문제

**현상**: 로그인 후 일부 API 호출 시 401, 일부는 403 반환 — 예측 불가 패턴

**원인 분석**:
1. `JwtAuthenticationFilter`에서 `Bearer ` prefix 파싱 누락 케이스 존재
2. `SecurityConfig` 인가 규칙 순서 문제 — 더 구체적인 경로가 `.anyRequest().authenticated()` 뒤에 선언되어 무시됨
3. `@AuthenticationPrincipal Long userId` 가 null 반환 — Security Context에 userId가 아닌 username이 등록됨

**해결**:
- 필터 내 토큰 파싱 로직 정비, prefix 없는 경우 조기 리턴
- SecurityConfig 인가 규칙을 **구체적 경로 → 광범위 경로** 순으로 재정렬
- `UsernamePasswordAuthenticationToken`의 principal을 `userId(Long)`로 통일

```
커밋: fix: jwt 인증 401/403 문제 해결 및 cors 설정 수정 (38b3427)
```

---

### 6-2. 로그아웃 500 에러

**현상**: `/api/auth/logout` 호출 시 500 Internal Server Error

**원인**: `@AuthenticationPrincipal Long userId`가 null — 로그아웃 요청의 토큰이 블랙리스트 처리 직후 Security Context에서 제거되는 타이밍 문제

**해결**: userId가 null일 때 Authorization 헤더에서 직접 토큰을 파싱해 userId를 재추출하는 방어 코드 추가

```java
Long resolvedUserId = userId;
if (resolvedUserId == null && bearerToken != null) {
    resolvedUserId = jwtTokenProvider.getUserId(bearerToken.substring(7));
}
```
```
커밋: 로그아웃 기능 500에러 수정 (b6cb71d)
```

---

### 6-3. CI/CD 파이프라인 구축 과정의 연쇄 오류

**현상**: GitHub Actions 최초 도입 후 5개 커밋 연속 실패

| 순서 | 오류 | 원인 | 해결 |
|------|------|------|------|
| 1 | `gradlew: Permission denied` | Linux 환경에서 실행 권한 없음 | `chmod +x ./gradlew` 스텝 추가 |
| 2 | `yaml 명칭 에러` | 파일명 오타 (`docker-image.yaml` vs `.yml`) | 파일명 통일 |
| 3 | `MySQL 연결 실패` | 서비스 컨테이너 헬스체크 전에 Gradle 빌드 시작 | `until mysqladmin ping` 대기 루프 추가 |
| 4 | `포트 에러` | 로컬 설정 3307 포트가 CI 환경 3306 충돌 | CI 환경변수 오버라이드 명시 |
| 5 | `docker network 에러` | prod compose에서 서비스 네트워크 이름 불일치 | 네트워크 명칭 통일 |

```
커밋: a0f4f4a → dbcfc87 → 853f9d2 → 2fc0e65 → 3d6484d → 4029152
```

---

### 6-4. Redis 역직렬화 오류

**현상**: 캐시에서 객체 조회 시 `InvalidDefinitionException: No suitable constructor`

**원인**: `GenericJackson2JsonRedisSerializer`는 역직렬화 시 기본 생성자(no-arg)가 필요한데, 엔티티/DTO에 `@NoArgsConstructor`가 없었음

**해결**: 캐시 대상 클래스에 Lombok `@NoArgsConstructor` 추가

```
커밋: 조회 메서드 Redis 도입 및 Redis 역직렬화로 인한 기본 생성자 설정 (d6c70af)
```

---

### 6-5. 게시판 병합 시 파라미터 불일치 + 권한 오류

**현상**: 프론트엔드와 통합 시 게시판 API가 400/403 반환

**원인**:
- 프론트가 `boardId`를 쿼리 파라미터로 전송, 백엔드는 PathVariable로 수신
- SecurityConfig에서 `/api/board/{id}` 경로가 인증 필요 규칙에 걸림

**해결**:
- API 계약 통일: PathVariable 방식으로 일원화
- 게시판 조회 경로를 `permitAll()` 명시적 선언

```
커밋: 게시판 기능 병합 에러(프론트와 파라미터 문제 + 백엔드 권한 문제 수정) (469746b)
```

---

### 6-6. Spring AOP 적용 후 순환 의존성 에러

**현상**: AOP 도입 직후 애플리케이션 기동 불가 (`BeanCurrentlyInCreationException`)

**원인**: `LogAspect`가 `LogTrace`를 주입받고, `LogTrace`가 포인트컷 대상 빈을 참조하는 순환 구조

**해결**: `LogTrace`를 별도 컴포넌트로 분리하고, AOP 범위를 Controller·Service 계층으로 명확히 한정

```
커밋: AOP 에러수정 (ab5d6ca)
```

---

## 7. 회고

### 잘한 점
- JWT + Redis 블랙리스트 조합으로 Stateless 구조에서도 강제 로그아웃 구현
- Docker Compose 환경을 로컬 개발 → 프로덕션까지 일원화하여 "내 컴에서만 됨" 문제 제거
- CI/CD 파이프라인을 처음부터 끝까지 직접 구축하며 각 실패 원인을 하나씩 해결

### 아쉬운 점 / 개선 방향
- Refresh Token 재발급 엔드포인트(`/api/auth/reissue`) 미구현 — Access Token 만료 시 재로그인 필요
- 단위 테스트 / 통합 테스트 커버리지 부족 — 현재 CI에서 테스트를 돌리지만 실제 비즈니스 로직 검증 코드가 적음
- CORS 허용 오리진이 `localhost:3000` 하드코딩 — 배포 도메인 대응 필요
- 관리자 페이지 권한 체크가 SecurityConfig 내 URL 기반 — 메서드 레벨 `@PreAuthorize` 도입 검토
