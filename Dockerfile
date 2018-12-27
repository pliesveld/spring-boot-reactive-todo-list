FROM openjdk:8-jre

RUN mkdir -p /opt/application/
WORKDIR /opt/application/

COPY ./build/libs/application-1.0.jar /opt/application/application.jar
RUN touch /opt/application/application.jar

CMD ["java", "-jar", "application.jar"]
