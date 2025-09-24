# 1단계: 빌드용 (Gradle + JDK)
FROM gradle:7.6.2-jdk17 AS builder

WORKDIR /app
COPY . .

# Gradle로 jar 빌드 (bootJar task)
RUN ./gradlew clean bootJar

# 2단계: 런타임 (경량 JDK or JRE)
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
