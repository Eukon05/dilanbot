FROM amazoncorretto:20.0.0

ARG JAR_FILE=*.jar
ADD target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
