FROM openjdk:17
COPY target/*.jar ewm_stats.jar
EXPOSE 9080
ENTRYPOINT ["java","-jar","/ewm_stats.jar"]