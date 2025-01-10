# Stage 1: Build
FROM maven:3.8-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder app/target/receipt-processor-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar
EXPOSE 7070
CMD ["java", "-jar", "app.jar"]