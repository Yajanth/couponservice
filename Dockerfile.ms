# -------- Stage 1: Build the application --------
FROM eclipse-temurin:17 AS builder

WORKDIR /app
        
# Install Maven temporarily and remove after build
RUN apt-get update && \
            apt-get install -y maven && \
            rm -rf /var/lib/apt/lists/*
        
COPY pom.xml ./
COPY src ./src
        
# Build the application without tests
RUN mvn clean package -DskipTests

# -------- Stage 2: Runtime image --------
FROM eclipse-temurin:17-alpine
        
WORKDIR /app
        
# Add a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
        
# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar /app/app.jar
        
# Set file permissions and ownership
RUN chown appuser:appgroup /app/app.jar
        
# Run as non-root user
USER appuser
        
EXPOSE 8080
        
# Use exec form to avoid shell and enable signal handling
ENTRYPOINT ["java", "-jar", "/app/app.jar"]