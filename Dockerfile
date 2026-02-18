# ────────────────────────────────────────────────
# Stage 1 : Build
# ────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Gradle Wrapper + 의존성 정의 파일 먼저 복사 (레이어 캐시 활용)
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# 실행 권한 부여
RUN chmod +x gradlew

# 의존성 다운로드만 수행 (소스 변경 시 재다운로드 방지)
RUN ./gradlew dependencies --no-daemon -q

# 소스 복사 후 빌드 (테스트 제외)
COPY src/ src/
RUN ./gradlew bootJar --no-daemon -x test

# ────────────────────────────────────────────────
# Stage 2 : Run
# ────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 빌드된 JAR만 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 8080 포트 노출
EXPOSE 8080

# 실행 (컨테이너 환경 변수로 DB 설정 주입)
ENTRYPOINT ["java", "-jar", "app.jar"]
