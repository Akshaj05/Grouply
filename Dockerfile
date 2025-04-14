# Start from an OpenJDK base image
FROM openjdk:17-jdk-slim

LABEL authors="akshajrk"

# Set the working directory
WORKDIR /app

COPY /target/demo-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
