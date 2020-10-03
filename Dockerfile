FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /usr/src/apps
COPY build/libs/*.jar nutritionist-service.jar

EXPOSE 8082
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=local","/usr/src/apps/nutritionist-service.jar"]
