FROM alpine/java:22-jre

# FROM alpine/java:21-jdk
# FROM alpine:latest
# RUN apk add --no-cache openjdk21


# Этап 1: Установка OpenJDK 21
# FROM alpine:latest AS build
# RUN apk add --no-cache openjdk21

# # Этап 2: Создание финального образа
# FROM alpine:latest
# # Копируем только необходимые файлы из этапа build
# COPY --from=build /usr/lib/jvm/java-21-openjdk /usr/lib/jvm/java-21-openjdk
#
# # Устанавливаем переменные окружения
# ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk
# ENV PATH=$JAVA_HOME/bin:$PATH
#
# WORKDIR /app

COPY ./target/order-1.0-SNAPSHOT.jar /usr/local/bin/order-1.0-SNAPSHOT.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/usr/local/bin/order-1.0-SNAPSHOT.jar"]
# ENTRYPOINT ["/bin/sh"]