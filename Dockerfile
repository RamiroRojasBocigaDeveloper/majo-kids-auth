# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia pom.xml primero para aprovechar el cache de capas de Docker
COPY pom.xml .

# Descarga todas las dependencias y plugins al .m2 local
# Esta capa se cachea mientras pom.xml no cambie
RUN mvn dependency:resolve dependency:resolve-plugins -B

# Copia el código fuente
COPY src ./src

# Compila reutilizando el .m2 ya populado
RUN mvn package -DskipTests -B

# Runtime stage - alpine es más liviano (~200MB menos)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia el JAR compilado del stage anterior
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto que usa Render por defecto
EXPOSE 10000

# Flags de JVM optimizados para contenedores con poca RAM (Render free: 512MB)
# UseContainerSupport: detecta correctamente los límites de memoria del contenedor
# MaxRAMPercentage: usa hasta el 75% de la RAM disponible para el heap
# TieredStopAtLevel=1: reduce el tiempo de compilación JIT en el arranque
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:TieredStopAtLevel=1", \
    "-Dspring.profiles.active=prod", \
    "-jar", "app.jar"]