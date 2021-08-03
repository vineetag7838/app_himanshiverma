FROM openjdk:11
RUN mkdir /app
WORKDIR /app
COPY target/assignment-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "assignment-0.0.1-SNAPSHOT.jar"]

