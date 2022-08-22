FROM openjdk:11-jdk-slim-buster
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} connectable.jar
ENTRYPOINT ["java","-jar", "-Duser.timezone=Asia/Seoul", "connectable.jar"]