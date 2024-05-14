FROM openjdk:11.0.8-jdk-slim-buster

## Environment
ARG APPLICATION_ENV=dev

## Generate Application folders.
RUN mkdir -p /app/config/ && mkdir -p /app/certificates/ && mkdir -p /opt/migrations/

## Application executable
ADD build/libs/devices-manager.jar /app/devices.manager.service.jar

## Default Configurations
ADD src/main/resources/application-prod.properties /app/config/application.properties
ADD src/main/resources/db/migration/${APPLICATION_ENV} /opt/migrations

## Add Certificates
ADD src/main/resources/certificates/atlasmqtt.client.crt /app/certificates/atlasmqtt.client.crt
ADD src/main/resources/certificates/atlasmqtt.client.key /app/certificates/atlasmqtt.client.key
ADD src/main/resources/certificates/ca.atlasmqtt.crt /app/certificates/ca.atlasmqtt.crt

## Entrypoints.
ENTRYPOINT ["java", "-jar", "/app/devices.manager.service.jar", "--spring.config.location=/app/config/application.properties"]
