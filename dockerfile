# Use an official OpenJDK runtime as a base image
FROM openjdk:17-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the build.gradle and settings.gradle files to the working directory
COPY build.gradle settings.gradle /app/

# Copy the entire project
COPY . /app

# Build the application with Gradle
RUN ./gradlew clean build

# Set the default command to run your application
CMD ["java", "-jar", "build/libs/receipt-processor-all.jar"]