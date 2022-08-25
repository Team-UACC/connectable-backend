FROM openjdk:11-jdk-slim-buster

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} connectable.jar

ARG PROFILE
ENV PROFILE_ENV=$PROFILE
ENTRYPOINT java -jar -Dspring.profiles.active=$PROFILE_ENV -Duser.timezone=Asia/Seoul connectable.jar