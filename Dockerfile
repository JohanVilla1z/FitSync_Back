# Stage 1: Build the application using JDK
# Using a specific version can improve build reproducibility
FROM eclipse-temurin:17.0.10_7-jdk-alpine AS build

WORKDIR /workspace/app

# Install necessary tools in one layer and clean up apk cache
RUN apk update && apk upgrade && \
  apk add --no-cache curl bash && \
  rm -rf /var/cache/apk/*

# Copy Gradle wrapper files first to leverage Docker cache
COPY gradle gradle
COPY gradlew ./
RUN chmod +x ./gradlew

# Copy build configuration files
COPY build.gradle settings.gradle ./

# Consider downloading dependencies as a separate layer if needed for caching,
# but build includes dependency resolution anyway.
# RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build the application, skip tests.
# Using --no-daemon might save some memory during build in constrained environments.
RUN ./gradlew build -x test --no-daemon

# Stage 2: Create the final lightweight runtime image using JRE
# Using a specific version can improve build reproducibility
FROM eclipse-temurin:17.0.10_7-jre-alpine

# Install minimal runtime dependencies and clean up apk cache
RUN apk update && apk upgrade && \
  apk add --no-cache tzdata curl && \
  rm -rf /var/cache/apk/*

# Create a non-root user and group for security
RUN addgroup -g 1000 appuser && \
  adduser -u 1000 -G appuser -h /app -s /sbin/nologin -D appuser

WORKDIR /app

# Copy the built JAR from the build stage
# Using a more specific pattern avoids potential issues if multiple JARs exist
COPY --from=build /workspace/app/build/libs/*-SNAPSHOT.jar app.jar

# Set ownership for the app directory to the non-root user
RUN chown -R appuser:appuser /app

# Switch to the non-root user
USER appuser

# Set default port (Railway will override this with its PORT variable, but good for local testing)
ENV PORT=8080

# Limita el heap de la JVM al 40% de la RAM del contenedor y reduce el Metaspace para evitar OOM
# Reduce aún más los threads de Tomcat para liberar memoria
# ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=40.0 -XX:MaxMetaspaceSize=128m -Xss256k -Djava.security.egd=file:/dev/./urandom -Dserver.tomcat.max-threads=5 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/heapdump.hprof -Xlog:gc*"

# Healthcheck to verify application status (ensure /api/health exists and returns 2xx)
# Increased start-period slightly to give more time for Spring Boot startup in limited env.
# Increased timeout slightly.
HEALTHCHECK --interval=30s --timeout=5s --start-period=90s --retries=3 \
  CMD curl -f http://localhost:${PORT}/api/health || exit 1

# Expose the port the application will run on (informational, Railway handles actual exposure)
EXPOSE ${PORT}

# Entrypoint to run the application, using the configured JAVA_OPTS
# This form ensures JAVA_OPTS is expanded by the shell before executing java
ENTRYPOINT java $JAVA_OPTS -jar /app/app.jar
