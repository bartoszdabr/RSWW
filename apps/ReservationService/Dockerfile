FROM gradle:8.1.1-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17-alpine3.14

EXPOSE 9000

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/ReservationService.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/ReservationService.jar"]