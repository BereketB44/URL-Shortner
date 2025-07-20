# Use a lightweight OpenJDK 17 base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built Spring Boot JAR file into the container
# Assuming the JAR is in 'target' and named 'your-app-name.jar' after building
# ***IMPORTANT: Replace 'your-app-name.jar' with the actual name of your JAR file***
COPY target/PorchPick-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app listens on (default is 8080)
EXPOSE 8080

# Command to run the Spring Boot application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]