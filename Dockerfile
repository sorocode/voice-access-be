# 1단계: Gradle 빌드 이미지 (ARM 호환)
FROM gradle:8.5-jdk17 AS build
WORKDIR /build

# 의존성 캐싱을 위한 설정 파일들 먼저 복사
COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle

# 의존성 캐시 빌드 (에러 무시)
RUN gradle build -x test --no-daemon || true

# 전체 소스 복사
COPY . .

# 애플리케이션 빌드
RUN gradle bootJar -x test --no-daemon

# 2단계: 실행용 경량 이미지
FROM eclipse-temurin:17
WORKDIR /app

# wait-for-it.sh 복사 및 권한 설정
COPY wait-for-it.sh ./wait-for-it.sh
RUN chmod +x ./wait-for-it.sh

# 빌드된 JAR 복사
COPY --from=build /build/build/libs/*.jar app.jar

# 포트 오픈 (Spring Boot 기본 포트)
EXPOSE 8080

# MySQL이 올라올 때까지 기다렸다가 Spring Boot 실행
ENTRYPOINT ["bash", "./wait-for-it.sh", "db:3306", "--", "java", "-jar", "app.jar"]