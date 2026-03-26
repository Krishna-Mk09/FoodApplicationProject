FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/ApiGatewayService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9000
ENTRYPOINT ["java","-jar","app.jar"]
