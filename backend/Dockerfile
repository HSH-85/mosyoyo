# 1. 베이스 이미지: OpenJDK 17 사용
FROM openjdk:17

# 2. JAR 파일 복사 (Gradle 빌드 후 생성된 파일)
COPY build/libs/*.jar app.jar

# 3. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
