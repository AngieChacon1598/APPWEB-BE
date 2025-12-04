FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /workspace/app/target/*.jar app.jar

COPY --from=builder /workspace/app/src/main/resources/Wallet_RestauranteLosPinos /app/wallet

# Render asigna el puerto automáticamente a través de la variable PORT
EXPOSE 8081

# Usar la variable PORT de Render, con fallback a 8081
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8081}"]
