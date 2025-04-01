FROM openjdk:17-jdk-slim

COPY target/couponservice-0.0.1-SNAPSHOT.jar .

WORKDIR /

ENTRYPOINT ["java", "-jar", "app.jar"]
