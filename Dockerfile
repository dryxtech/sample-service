FROM openjdk:8-jre-alpine

VOLUME /tmp
EXPOSE 8080

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/dryxtech/sample-service.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:+MaxRAMPercentage=70.0"

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/dryxtech/sample-service.jar"]
