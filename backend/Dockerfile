FROM gradle:8.6 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM ubuntu:20.04

EXPOSE 8090

RUN apt-get update && apt-get install -y openjdk-17-jdk

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/backend-0.0.1-timetable-backend.jar /app/backend-0.0.1-timetable-backend.jar

ENTRYPOINT ["java", "-jar", "/app/backend-0.0.1-timetable-backend.jar"]
