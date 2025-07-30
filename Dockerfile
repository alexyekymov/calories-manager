FROM eclipse-temurin:21-alpine AS build

WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle ./
COPY settings.gradle ./

RUN chmod +x gradlew
RUN ./gradlew dependencies --refresh-dependencies --dry-run

COPY src ./src

RUN ./gradlew clean bootJar -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]