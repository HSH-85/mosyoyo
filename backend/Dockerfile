# 1) Build 스테이지 - Gradle CLI 사용
FROM gradle:8.2-jdk17 AS build
WORKDIR /app

# (A) 의존성 해상도에 필요한 메타파일만 우선 복사
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/

# (B) 소스 전체 복사
COPY . .

# (C) Gradle 데몬 없이 바로 빌드
RUN gradle clean bootJar -x test --no-daemon

# 2) Runtime 스테이지
FROM eclipse-temurin:17-jre
WORKDIR /app

# (D) 빌드된 JAR 복사
COPY --from=build /app/build/libs/*.jar app.jar

# (E) 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
