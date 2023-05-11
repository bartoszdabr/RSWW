FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN cd payment-read-service && mvn clean package

FROM amazoncorretto:17.0.7 AS payment-read-service-img
COPY --from=build  app/payment-read-service/target/payment-read-service-0.0.1.jar payment-read-service.jar
CMD ["java", "-jar", "payment-read-service.jar"]