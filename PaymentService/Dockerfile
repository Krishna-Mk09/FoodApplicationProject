FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/PaymentService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","app.jar"]
