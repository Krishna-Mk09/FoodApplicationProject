FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/NotificationService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8182
ENTRYPOINT ["java","-jar","app.jar"]
