# Use Amazon Corretto 17 SDK as the base image
FROM amazoncorretto:17-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven or Gradle build files (pom.xml or build.gradle) into the container
COPY pom.xml /app/pom.xml
COPY src /app/src

# Install dependencies and build the application
RUN apk add --no-cache maven

# Build the application (assuming you're using Maven)
RUN mvn clean package -DskipTests

# Expose the application port
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "target/articles-0.0.1-SNAPSHOT.jar"]
