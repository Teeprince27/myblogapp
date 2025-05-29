# MY Blog App


## Overview
This solution provides a RESTful API built with Spring Boot for blog content management with JWT authentication, admin approval workflow.

## Technology Stack

Framework: Spring Boot 3.5.x
Database: h2 db with Spring Data JPA
Authentication: Spring Security with JWT
Cloud Platform: Heroku
Testing: JUnit 5 + MockMvc
Documentation: OpenAPI 3 (Swagger)



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


myblogapp/
├── src/
│   ├── main/
│   │   ├── java/com/temitope/myblogapp/
│   │   │   ├── MyBlogApplication.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtConfig.java
│   │   │   │   └── SwaggerConfig.java
|           |── constant/
│   │   │   │   ├── ResponseCode.java
│   │   │   │   
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── BlogController.java
│   │   │   │   └── AdminController.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   
│   │   │   ├── enums/
│   │   │   │   ├── UserRole.java
│   │   │   │   └── PostStatus.java
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java
├           |── model/
│   │   │   │   ├── User.java
│   │   │   │   └── BlogPost.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   └── BlogPostRepository.java
│   │   │   ├── security/
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   └── UserPrincipal.java
│   │   │   └── service/
│   │   │       ├── AuthService.java
│   │   │       ├── BlogService.java
│   │   │       └── AdminService.java
│   │   └── resources/
│   │       ├── application.yml
│   │      
│   └── test/
│       └── java/com/temitope/myblogapp/
        |_ BlogControllerTest
        |_ MyBlogApplicationTests
├── pom.xml
└── README.md