FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY run.sh .
COPY target/*.jar app.jar
ENTRYPOINT ["run.sh"]