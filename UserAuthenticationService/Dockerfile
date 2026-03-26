FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/UserAuthenticationService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","app.jar"]


