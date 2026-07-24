# ---------- Stage 1: Build ----------
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

RUN apk add --no-cache maven

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests -B

# ---------- Stage 2: Production runner ----------
FROM eclipse-temurin:17-jre-jammy AS runner
WORKDIR /app

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/*.jar app.jar

RUN groupadd -r spring && useradd -r -g spring spring
USER spring

ENV SPRING_PROFILES_ACTIVE=production

EXPOSE 8086

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8086/actuator/health || exit 1

CMD ["java", "-jar", "/app/app.jar"]