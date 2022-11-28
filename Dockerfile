FROM openjdk:17-jdk-alpine

FROM maven:3.8.5-openjdk-17

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

CMD /wait && java -jar /app.jar