FROM openjdk:17
COPY build/libs/user-service.jar user-service.jar
ENTRYPOINT ["java", "-jar", "/user-service.jar"]