FROM alpine/java:21-jdk
COPY ./target/pub-user-1.0-SNAPSHOT.jar /usr/local/bin/pub-user-1.0-SNAPSHOT.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/usr/local/bin/pub-user-1.0-SNAPSHOT.jar"]