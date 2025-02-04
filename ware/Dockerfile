FROM alpine/java:22-jre

COPY ./target/ware-1.0-SNAPSHOT.jar /usr/local/bin/ware-1.0-SNAPSHOT.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/usr/local/bin/ware-1.0-SNAPSHOT.jar"]
# ENTRYPOINT ["/bin/sh"]