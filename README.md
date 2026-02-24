# Task Tracker

[![Actions Status](https://github.com/streltsov95/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/streltsov95/java-project-99/actions)
[![Build CI](https://github.com/streltsov95/java-project-99/actions/workflows/main.yml/badge.svg)](https://github.com/streltsov95/java-project-99/actions/workflows/main.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=streltsov95_java-project-99&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=streltsov95_java-project-99)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=streltsov95_java-project-99&metric=coverage)](https://sonarcloud.io/summary/new_code?id=streltsov95_java-project-99)

🔗 **Application:** https://task-tracker-cm9k.onrender.com  
📘 **OpenAPI documentation:** https://task-tracker-cm9k.onrender.com/swagger-ui.html

---

## 📌 Description

**Task Tracker** — production-ready fullstack task management application built with Spring Boot.

The application exposes a REST API secured with JWT authentication and includes a frontend bundled as static resources. It supports task management with filtering capabilities and integrates error monitoring via Sentry.

The project follows production-oriented practices including CI/CD automation, static code analysis, test coverage reporting, and containerized deployment.

---

## 🚀 Features

- JWT-based authentication
- CRUD operations for:
    - `Task`
    - `Label`
    - `TaskStatus`
    - `User`
- Task filtering
- OpenAPI (Swagger UI) documentation
- Centralized error monitoring (Sentry)
- CI pipeline with GitHub Actions
- Code quality analysis with SonarCloud

---

## 🏗 Tech Stack

### Backend
- Java 21
- Spring Boot 3.5.3
- Spring Web
- Spring Data JPA
- Spring Security
- OAuth2 Resource Server (JWT)
- MapStruct
- Hibernate

### Databases
- H2 (development)
- PostgreSQL (production)

### API & Documentation
- springdoc-openapi (Swagger UI)

### Observability
- Sentry

### Testing
- JUnit 5
- Spring Boot Test
- Instancio
- JsonUnit

### DevOps
- Gradle 8
- GitHub Actions
- SonarCloud
- Docker
- Render (deployment)

---

## ⚙️ Running Locally

### 1️⃣ Clone the repository

```bash
git clone https://github.com/streltsov95/java-project-99.git
cd java-project-99
```

### 2️⃣ Build the project

Using Make:
```bash
make build
```
Or using Gradle directly:
```bash
./gradlew build
```

### 3️⃣ Run the application (development profile)
```bash
./gradlew bootRun
```
The application will start at: `http://localhost:8080`

H2 console (development only): `http://localhost:8080/h2-console`

---
## 🐳 Running with Docker
Build image: 
```bash
docker build -t task-tracker .
```
Run container:
```bash
docker run -p 8080:8080 task-tracker
```
---
## 🔐 Environment Variables (Production Profile)

When running with the production profile, the following environment variables must be provided:

| Variable            | Description                      |
| ------------------- | -------------------------------- |
| `JDBC_DATABASE_URL` | PostgreSQL JDBC URL              |
| `USERNAME`          | Database username                |
| `PASSWORD`          | Database password                |
| `PORT`              | Application port (default: 8080) |
| `SENTRY_DSN`        | Sentry DSN for error monitoring  |
---
## 🔑 Authentication

The application uses JWT-based authentication.

After successful authentication, include the JWT token in requests: 
```
Authorization: Bearer <token>
```
OpenAPI UI can be used to authorize requests directly via Swagger interface.

---
## 🧪 Testing
Run tests:

```bash
make test
```

Or:
```bash
./gradlew test
```
---
## 📊 CI / Quality Control

The project includes:

- GitHub Actions pipeline (build, lint, test)

- SonarCloud static code analysis
---
## 🧠 Error Monitoring

The application integrates with Sentry for real-time exception tracking in production environment.

---
## 📄 License

This project is created for educational and portfolio purposes.