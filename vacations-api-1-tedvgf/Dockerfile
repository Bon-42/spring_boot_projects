# Stage 1: Build using Gradle Wrapper
FROM openjdk:17-slim as build

# Set the working directory in Docker
WORKDIR /app

# Copy Gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy the source code
COPY src src

# Grant execution permissions on the gradlew script
RUN chmod +x ./gradlew

# Build the application using the Gradle wrapper
RUN ./gradlew build -x test

# Stage 2: Run the built JAR file
FROM openjdk:17-slim

# Expose the port the app runs on
EXPOSE 8080

# Copy the JAR file from the build stage to the /app directory
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Run the JAR file
ENTRYPOINT ["java","-jar","/app/app.jar"]
