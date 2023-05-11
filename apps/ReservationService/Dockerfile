FROM gradle:8.1.1-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build --no-daemon

FROM openjdk:17-alpine3.14 as reservation-read-service

EXPOSE 9000

RUN mkdir /app

COPY --from=build /home/gradle/src/reservation-read-service/build/libs/reservation-read-service-0.0.1-SNAPSHOT.jar /app/ReservationReadService.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/ReservationReadService.jar"]

FROM openjdk:17-alpine3.14 as reservation-write-service

EXPOSE 9001

RUN mkdir /app

COPY --from=build /home/gradle/src/reservation-write-service/build/libs/reservation-write-service-0.0.1-SNAPSHOT.jar /app/ReservationWriteService.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/ReservationWriteService.jar"]
