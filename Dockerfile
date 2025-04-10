FROM eclipse-temurin:17-jdk-alpine3.18 AS build

# Actualizar paquetes e instalar dependencias necesarias
RUN apk update && apk upgrade && \
  apk add --no-cache curl bash && \
  rm -rf /var/cache/apk/*

WORKDIR /workspace/app

# Copiar archivos del proyecto
COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./
COPY src src

# Dar permisos de ejecución al gradlew
RUN chmod +x ./gradlew

# Construir la aplicación
RUN ./gradlew build -x test
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

# Etapa final con JRE más ligero
FROM eclipse-temurin:17-jre-alpine3.18

# Actualizar paquetes e instalar dependencias mínimas necesarias
RUN apk update && apk upgrade && \
  apk add --no-cache tzdata && \
  rm -rf /var/cache/apk/*

# Crear un usuario no privilegiado
RUN addgroup -g 1000 appuser && \
  adduser -u 1000 -G appuser -h /app -s /sbin/nologin -D appuser

WORKDIR /app
VOLUME /tmp

# Copiar los archivos compilados desde la etapa de build
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Configurar permisos apropiados
RUN chown -R appuser:appuser /app
USER appuser

# Healthcheck para verificar que la aplicación esté funcionando
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${PORT:-8080}/api/health || exit 1

# Configuración de seguridad adicional
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java","-cp","app:app/lib/*","com.johan.gym_control.GymControlApplication"]