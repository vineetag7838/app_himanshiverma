FROM openjdk:11
RUN mkdir /app
WORKDIR /app
COPY target/nagp-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "nagp-0.0.1-SNAPSHOT.jar"]

