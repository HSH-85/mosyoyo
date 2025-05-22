FROM gradle:8.2-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/
RUN gradle --no-daemon clean bootJar

COPY . .
RUN gradle --no-daemon bootJar -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]