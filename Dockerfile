# =================================================================
# 1. 빌드 스테이지 (Builder Stage)
# =================================================================
# arm64를 지원하는 non-alpine gradle 이미지 사용
FROM gradle:8.5.0-jdk17 AS builder

WORKDIR /home/gradle/src

COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle build --no-daemon --stacktrace || return 0

COPY src ./src
RUN gradle build --no-daemon --stacktrace


# =================================================================
# 2. 최종 실행 스테이지 (Final Stage)
# =================================================================
# 수정된 부분: arm64를 확실하게 지원하는 공식 OpenJDK의 경량(slim) 이미지로 교체
# 이 이미지는 JRE가 아닌 JDK를 포함하지만, slim 버전이라 가볍고 플랫폼 지원이 확실합니다.
FROM openjdk:17-slim

WORKDIR /app

COPY --from=builder /home/gradle/src/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]