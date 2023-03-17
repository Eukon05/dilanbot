FROM amazoncorretto:19.0.2

ARG JAR_FILE=*.jar
ADD target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
