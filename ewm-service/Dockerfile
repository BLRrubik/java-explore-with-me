FROM openjdk:17
COPY target/*.jar ewm_service.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","/ewm_service.jar"]