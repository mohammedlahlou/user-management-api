# 👤 User Management API

A production-ready RESTful API for user management built with **Spring Boot**, **Spring Security (JWT)**, **PostgreSQL**, and fully documented with **Swagger/OpenAPI**. Deployable via **Docker**.

---

## 🚀 Features

- ✅ User registration & login with JWT authentication
- ✅ Role-based access control (ADMIN / USER)
- ✅ CRUD operations on users (admin only)
- ✅ Password encryption with BCrypt
- ✅ Input validation & global exception handling
- ✅ Swagger UI for interactive API documentation
- ✅ Dockerized for easy deployment
- ✅ Unit & integration tests

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.x |
| Security | Spring Security + JWT (jjwt) |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Documentation | Swagger / OpenAPI 3 |
| Containerization | Docker & Docker Compose |
| Build Tool | Maven |
| Java Version | Java 17 |

---

## 📁 Project Structure

```
user-management-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/usermanagement/
│   │   │   ├── config/          # Security & Swagger config
│   │   │   ├── controller/      # REST Controllers
│   │   │   ├── dto/             # Request / Response DTOs
│   │   │   ├── entity/          # JPA Entities
│   │   │   ├── exception/       # Global exception handler
│   │   │   ├── repository/      # Spring Data JPA Repositories
│   │   │   ├── security/        # JWT Filter & Utils
│   │   │   └── service/         # Business Logic
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-docker.yml
│   └── test/                    # Unit & Integration Tests
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

---

## ⚡ Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose (optional)

### Run with Docker (recommended)

```bash
git clone https://github.com/mohammedlahlou/user-management-api.git
cd user-management-api
docker-compose up --build
```

API will be available at: `http://localhost:8080`  
Swagger UI: `http://localhost:8080/swagger-ui.html`

### Run locally

```bash
# 1. Configure your PostgreSQL connection in application.yml
# 2. Build and run
mvn spring-boot:run
```

---

## 🔐 Authentication Flow

```
POST /api/auth/register   → Register a new user
POST /api/auth/login      → Get JWT token
GET  /api/users           → List users (requires ADMIN role)
GET  /api/users/{id}      → Get user by ID (authenticated)
PUT  /api/users/{id}      → Update user (ADMIN or own profile)
DELETE /api/users/{id}    → Delete user (ADMIN only)
```

Include the token in all protected requests:
```
Authorization: Bearer <your_jwt_token>
```

---

## 📖 API Documentation

Full interactive documentation available via Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Running Tests

```bash
mvn test
```

---

## 🐳 Docker Configuration

```yaml
# docker-compose.yml overview
services:
  app:   # Spring Boot app on port 8080
  db:    # PostgreSQL on port 5432
```

---

## 📌 Environment Variables

| Variable | Description | Default |
|---|---|---|
| `DB_HOST` | PostgreSQL host | `localhost` |
| `DB_PORT` | PostgreSQL port | `5432` |
| `DB_NAME` | Database name | `userdb` |
| `DB_USERNAME` | DB username | `postgres` |
| `DB_PASSWORD` | DB password | `postgres` |
| `JWT_SECRET` | JWT signing secret | (required) |
| `JWT_EXPIRATION` | Token expiry in ms | `86400000` |

---

## 🤝 Author

**Mohammed Lahlou**  
Freelance Java/Spring Boot Developer  
📧 lahloumohammed28@gmail.com  
🔗 [LinkedIn](https://www.linkedin.com/in/lahloumohammed/) | [Upwork](https://upwork.com/yourprofile)

---

