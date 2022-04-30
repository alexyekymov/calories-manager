# Build stage

FROM maven:3.8.3-jdk-8-slim as build

COPY src /home/app/src
COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean package

# Package stage

FROM tomcat:9-jdk8-openjdk

COPY --from=build /home/app/target/calories-manager.war /usr/local/tomcat/webapps

EXPOSE 8080

CMD ["catalina.sh", "run"]



