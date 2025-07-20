# Stage 1: Build the JAR
FROM maven:3.9.4-eclipse-temurin-17 as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/PorchPick-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
