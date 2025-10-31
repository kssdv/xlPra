# =================================================================
# 1. 빌드 스테이지 (Builder Stage)
# =================================================================
# Java 17을 포함한 Gradle 이미지를 빌드 환경으로 사용합니다.
FROM gradle:8.5.0-jdk17-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /home/gradle/src

# 빌드에 필요한 파일들만 먼저 복사하여 의존성 캐싱을 활용합니다.
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# 먼저 의존성을 다운로드하여 레이어에 캐싱합니다.
# 소스 코드가 변경되어도 의존성은 다시 받지 않아 빌드 속도가 향상됩니다.
RUN gradle build --no-daemon --stacktrace || return 0

# 나머지 소스 코드를 복사합니다.
COPY src ./src

# 애플리케이션을 빌드하고 실행 가능한 JAR 파일을 생성합니다.
# --no-daemon 옵션은 Docker 환경에서 권장됩니다.
RUN gradle build --no-daemon --stacktrace


# =================================================================
# 2. 최종 실행 스테이지 (Final Stage)
# =================================================================
# Java 17 실행 환경(JRE)만 포함된 더 가벼운 이미지를 사용합니다.
FROM openjdk:17-jre-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일을 최종 이미지로 복사합니다.
# build/libs/*.jar 패턴은 Gradle이 생성하는 기본 JAR 파일을 찾습니다.
COPY --from=builder /home/gradle/src/build/libs/*.jar app.jar

# 애플리케이션이 사용하는 포트를 명시합니다 (문서화 목적)
EXPOSE 8080

# 컨테이너가 시작될 때 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]