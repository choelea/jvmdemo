FROM openjdk:8-jdk-alpine
COPY run.sh .
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["run.sh"]