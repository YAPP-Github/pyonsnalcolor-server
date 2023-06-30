FROM openjdk:11
COPY pyonsnalcolor-api/build/libs/pyonsnalcolor-api-1.0-SNAPSHOT.jar api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/api.jar"]

RUN echo "pyonsnalcolor server start"