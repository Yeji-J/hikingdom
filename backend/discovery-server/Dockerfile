FROM openjdk:11-jdk
VOLUME /tmp
ARG JAR_FILE=build/libs/discovery-server.jar
COPY ${JAR_FILE} discovery-server.jar

ARG SERVER_MODE
RUN echo "$SERVER_MODE"
ENV SERVER_MODE=$SERVER_MODE

EXPOSE 8761

ENTRYPOINT ["java", "-Dspring.profiles.active=${SERVER_MODE}","-Duser.timezone=Asia/Seoul", "-jar", "/discovery-server.jar"]
