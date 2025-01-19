FROM alpine/java:21-jdk
COPY ./target/app.jar /usr/local/bin/app-1.0-SNAPSHOT.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/usr/local/bin/app-1.0-SNAPSHOT.jar"]