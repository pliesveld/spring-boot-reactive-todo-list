FROM openjdk:8-jre

RUN mkdir -p /opt/application/
WORKDIR /opt/application/

COPY ./build/libs/application-1.0.jar /opt/application/application.jar
RUN touch /opt/application/application.jar

# ENV JAVA_TOOL_OPTIONS "-XX:+PrintFlagsFinal -XX:+UnlockDiagnosticVMOptions"
ENV JAVA_TOOL_OPTIONS "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:OnOutOfMemoryError='shutdown -r' -XX:+UseStringDeduplication -XX:+UseStringCache -XX:+UseCompressedStrings -XX:+OptimizeStringConcat"

CMD ["java", "-jar", "application.jar"]
