FROM gradle:jdk-21-and-24-alpine AS build

WORKDIR /app

COPY build.gradle ./
COPY settings.gradle ./

RUN gradle dependencies --refresh-dependencies

COPY src ./src

RUN gradle clean bootJar -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]