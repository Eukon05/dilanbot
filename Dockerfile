FROM amazoncorretto:21.0.1

ARG JAR_FILE=*.jar
ADD target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
