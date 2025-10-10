# Task Manager API

Task Manager API is a Spring Boot REST application for managing tasks easily. It exposes endpoints to create, retrieve, update, and delete tasks, as well as filters by completion status and by title. The project ships with a permissive security setup for local testing and centralized error handling to return consistent responses.

## Key Features

- **CRUD task management** with fields for title, description, completion status, and due date.
- **Persistence with Spring Data JPA** using an in-memory H2 database for development.
- **Input validation** powered by `jakarta.validation` annotations.
- **Centralized exception handling** that returns structured error messages.
- **OpenAPI/Swagger UI documentation** provided by `springdoc-openapi`.
- **Actuator support** to expose basic application metrics.

## Prerequisites

- Java 21 or later (the project compiles with language level 21 and preview features up to level 24).
- Maven 3.9 or newer.

## Run Locally

```bash
mvn spring-boot:run
```

After the application starts, the API is available at `http://localhost:8080`.

### H2 Console

The H2 console is enabled at `http://localhost:8080/h2-console`. Use the JDBC URL `jdbc:h2:mem:testdb` with user `sa` and no password by default.

### OpenAPI Documentation

Explore the API through Swagger UI at `http://localhost:8080/swagger-ui.html`.

## Main Endpoints

| Method | Path | Description |
| ------ | ---- | ----------- |
| `GET` | `/api/tasks` | List all tasks. |
| `GET` | `/api/tasks/{id}` | Retrieve a task by its identifier. |
| `POST` | `/api/tasks` | Create a new task. |
| `PATCH` | `/api/tasks/{id}` | Update fields of an existing task. |
| `DELETE` | `/api/tasks/{id}` | Delete a task by its identifier. |
| `GET` | `/api/tasks/completed` | Return tasks marked as completed. |
| `GET` | `/api/tasks/title?title=...` | Search for tasks by exact title match. |

## Run Tests

```bash
mvn test
```

## Security

The current security configuration allows unauthenticated access to all endpoints and disables CSRF to simplify development testing. Adjust `SecurityConfig` before using the API in production.

## Project Structure

```
src/
├── main
│   ├── java/com/javiersillo/taskmanager
│   │   ├── config        # Security configuration
│   │   ├── controller    # REST controllers
│   │   ├── exception     # Custom exceptions and global handler
│   │   ├── model         # JPA entities
│   │   ├── repository    # JPA repository interfaces
│   │   └── service       # Business logic
│   └── resources
│       └── application.properties
└── test
    └── java/com/javiersillo/taskmanager
        ├── controller    # Controller tests
        ├── service       # Service tests
        └── TaskManagerApiApplicationTests.java
```

## License

This project is distributed under the terms defined by its original author. Update this section if you plan to publish the code with a specific license.
