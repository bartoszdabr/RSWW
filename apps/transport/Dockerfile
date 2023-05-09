FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app

COPY . .

RUN mvn clean package

FROM amazoncorretto:17.0.7 AS transport-read-service-img
WORKDIR /app
COPY --from=build /app/transport-read-service/target/transport-read-service-0.0.1.jar app.jar
CMD ["java", "-jar", "app.jar"]

FROM amazoncorretto:17.0.7 AS transport-write-service-img
WORKDIR /app
COPY --from=build /app/transport-write-service/target/transport-write-service-0.0.1.jar app.jar
CMD ["java", "-jar", "app.jar"]