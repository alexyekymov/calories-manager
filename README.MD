[![Docker Build](https://github.com/alexyekymov/calories-manager/actions/workflows/docker-build.yml/badge.svg)](https://github.com/alexyekymov/calories-manager/actions/workflows/docker-build.yml)
<p align="center">
 <img src="./assets/logo_v1.svg" alt="CM logo"/>
</p>

# Calories manager
A lightweight calorie tracker featuring basic authentication and role-based access control.
Admins can manage users, while regular users can manage their own profile and meal data via a REST API with basic authentication. Food entries can be filtered by date and time.

## Demo
The application is deployed and available as a demo. You can explore the API using Swagger UI: [Open Swagger](https://calories-manager.overlax.pp.ua/)

## Tech Stack
- Java 21
- Spring (Boot, Web, Security, Data JPA)
- PostgreSQL, Flyway, Caffeine
- MapStruct, Lombok
- JUnit 5, Testcontainers
- Gradle, Docker, GitHub Actions

## Local Development
If you already have a PostgreSQL instance for development, update the .env with your credentials

If you don't have a running PostgreSQL instance, you can run it in docker:
```shell
  docker compose up -d
```
Then run the application using your IDE, or the Gradle wrapper:
```shell
  ./gradlew bootRun -x test
```
## Production Deployment
Update the credentials in the `.env` file with stronger credentials and run:
```shell
  docker compose -f docker-compose.prod.yml up -d
```

## Roadmap:
- [x] Basic auth security
- [x] Caching
- [x] Exception handling
- [ ] Logging
- [ ] Tests
- [ ] JWT-based Security
