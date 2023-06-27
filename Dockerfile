FROM openjdk:11
COPY pyonsnalcolor-member/build/libs/pyonsnalcolor-member-1.0-SNAPSHOT.jar member.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/member.jar"]

RUN echo "pyonsnalcolor-member start"