# Step 1: Use an official OpenJDK base image from Docker Hub
FROM eclipse-temurin:21-jre-jammy

# Step 3: Copy the Spring Boot JAR file into the container
ADD build/libs/RedisCache.jar RedisCache.jar

# Step 4: Expose the port your application runs on
EXPOSE 8080

# Step 5: Define the command to run your Spring Boot application
CMD ["java", "-jar", "/RedisCache.jar"]