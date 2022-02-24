FROM adoptopenjdk/openjdk11:alpine-jre
LABEL maintainer="lchhung@gmail.com"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app.jar"]