# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-24 AS build
WORKDIR /app

# Leverage layer caching
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests verify || true

# Copy sources and build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests clean package

# ---------- Runtime stage ----------
FROM eclipse-temurin:24-jre
WORKDIR /app

# Non-root user for better security
RUN useradd -r -u 1001 -g root appuser
USER appuser

# Copy fat jar
COPY --from=build /app/target/*-SNAPSHOT.jar /app/app.jar

# Expose default Spring Boot port
EXPOSE 8080

# JVM options can be overridden by env var JAVA_TOOL_OPTIONS
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java","-jar","/app/app.jar"]
