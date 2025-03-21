# Use OpenJDK 11 as the base image
FROM amazoncorretto:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/articles-0.0.1-SNAPSHOT.jar /app/articles-0.0.1-SNAPSHOT.jar

# Expose the application port (change this to your app's port)
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "articles-0.0.1-SNAPSHOT.jar"]
