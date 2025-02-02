FROM alpine/java:22-jre

# FROM alpine/java:21-jdk
# FROM alpine:latest
# RUN apk add --no-cache openjdk21
# WORKDIR /app

COPY ./target/billing-1.0-SNAPSHOT.jar /usr/local/bin/billing-1.0-SNAPSHOT.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/usr/local/bin/billing-1.0-SNAPSHOT.jar"]