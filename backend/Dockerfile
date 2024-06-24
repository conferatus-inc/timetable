FROM gradle:8.6 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM amazoncorretto:17

EXPOSE 8090

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/backend-0.0.1-timetable-backend.jar /app/backend-0.0.1-timetable-backend.jar

ENTRYPOINT ["java", "-jar", "/app/backend-0.0.1-timetable-backend.jar"]

#a
