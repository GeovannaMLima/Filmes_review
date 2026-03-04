FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/Cinema-0.0.1-SNAPSHOT.jar /app/Cinema.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/Cinema.jar"]
