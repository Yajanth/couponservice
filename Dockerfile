FROM openjdk:17-jdk-slim

COPY target/couponservice-0.0.1-SNAPSHOT.jar .

WORKDIR /

# RUN java -jar couponservice-0.0.1-SNAPSHOT.jar


CMD [ "echo this is working" ]