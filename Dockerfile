FROM openjdk:17
COPY build/libs/myApp.jar myApp.jar
ENTRYPOINT ["java", "-jar", "/myApp.jar"]