# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy gradle wrapper and build files first (better layer caching)
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts gradle.properties ./

RUN chmod +x ./gradlew

# Copy source code
COPY src ./src

# Build a self-contained runnable "fat jar" (provided by the Ktor Gradle plugin)
RUN ./gradlew buildFatJar --no-daemon

# ---------- Run stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/build/libs/*-all.jar app.jar

# Railway injects PORT at runtime; application.yaml reads it via ${PORT:8080}
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
