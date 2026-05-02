# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the project files
COPY pom.xml .
COPY src ./src

# Build the application skipping tests to speed up the process
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the jar file from the build stage
# The wildcard matches the version number so we don't need to hardcode it
COPY --from=build /app/target/*.jar app.jar

# Expose the port defined in application.properties (server.port=80)
EXPOSE 80

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
