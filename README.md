# MY Blog App

## Development Tool
1. Java 17
2. Spring-Boot 3.5.x


## 🚀 Live Demo

API Base URL: {{Baseurl}}/api
Health Check: {{Baseurl}}/health
API Documentation: {{Baseurl}}/docs


## 🚀 Deployment
Docker Deployment
Dockerfile
dockerfileFROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/myblogapp-*.jar /opt/myblogapp/myblogapp.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "myblogapp.jar"]