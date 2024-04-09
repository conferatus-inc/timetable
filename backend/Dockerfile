FROM gradle:8.6 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM ubuntu:20.04

EXPOSE 8090

RUN apt-get update && apt-get install -y openjdk-17-jdk

RUN apt-get update && \
    apt-get install wget postgresql-client --yes && \
    mkdir --parents ~/.postgresql && \
    wget "https://storage.yandexcloud.net/cloud-certs/CA.pem" \
         --output-document ~/.postgresql/root.crt && \
    chmod 0600 ~/.postgresql/root.crt

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/backend-0.0.1-timetable-backend.jar /app/backend-0.0.1-timetable-backend.jar

ENTRYPOINT ["java", "-jar", "/app/backend-0.0.1-timetable-backend.jar"]
