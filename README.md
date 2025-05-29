# MY Blog App

## Development Tool
1. Java 17
2. Spring-Boot 3.5.x


## 🚀 Live Demo
Baseurl= https://myblogapp-eebb120c9c75.herokuapp.com/myblog

API Base URL: {{Baseurl}}/api
Health Check: {{Baseurl}}/health
API Documentation: {{Baseurl}}/swagger-ui/index.html


## 🚀 Deployment
Docker Deployment
Dockerfile
dockerfileFROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/myblogapp-*.jar /opt/myblogapp/myblogapp.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "myblogapp.jar"]