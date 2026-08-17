# 🦖 Pokedex Backend API

This is the backend REST API for the Pokedex application. Built with **Java 17** and **Spring Boot**, it provides a robust service to manage Pokemon data, utilizing **PostgreSQL** as its persistent database.

---

## 🛠️ Tech Stack & Libraries

*   **Java 17** (Amazon Corretto / Eclipse Temurin)
*   **Spring Boot 3.x** (Web, Data JPA)
*   **PostgreSQL** (Relational Database)
*   **MapStruct** (Type-safe bean mapping between Entities and DTOs)
*   **Hibernate** (ORM Framework)
*   **Maven** (Project Management and Build Tool)

---

## 🚀 Deployment with Docker (Recommended)

The project includes a **multi-stage build Dockerfile** and a **Docker Compose** configuration to spin up both the application and the PostgreSQL database automatically.

### Prerequisites
Make sure you have **Docker** and **Docker Compose** installed on your machine.

### Steps to Run
1. Navigate to the project root directory (where `docker-compose.yml` is located).
2. Run the following command to build the Java application and start the containers:
   ```bash
   docker-compose up --build
   ```

The system will setup:
*   **PostgreSQL Database** running on port `5432`.
*   **Spring Boot API** running on port `8080`.

---

## 💻 Local Development (Without Docker)

If you prefer to run the application locally outside of Docker:

### 1. Database Configuration
Ensure you have a local PostgreSQL instance running. Update your `src/main/resources/application.yaml` file with your local database credentials:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres?createDatabaseIfNotExist=true
    username: your_postgres_user
    password: your_postgres_password
```

### 2. Build and Run
Use the Maven wrapper to compile and start the application:
```bash
# On Windows
mvnw spring-boot:run

# On Mac/Linux
./mvnw spring-boot:run
```

The API will be accessible at: `http://localhost:8080`

---

## 🔌 API Endpoints (Base URL: `/api/v1/pokemon`)

The service exposes the following REST endpoints to interact with the Pokedex system. It handles validation and utilizes specific Request/Response DTOs:

| Method | Endpoint | Description | Request Body | Response Body |
| :--- | :--- | :--- | :--- | :--- |
| **GET** | `/api/v1/pokemon` | Retrieve all Pokemons. | *None* | `List<PokemonResponseDTO>` |
| **GET** | `/api/v1/pokemon/pagination` | Retrieve Pokemons with offset-based pagination (Defaults: offset=0, limit=8). | *Query Params* | `List<PokemonResponseDTO>` |
| **GET** | `/api/v1/pokemon/{id}` | Retrieve a single Pokemon by its ID. | *None* | `PokemonResponseDTO` |
| **POST** | `/api/v1/pokemon` | Create a new Pokemon (Validates input). | `PokemonRequestDTO` | `PokemonResponseDTO` |
| **PUT** | `/api/v1/pokemon/{id}` | Fully update an existing Pokemon (Validates input). | `PokemonRequestDTO` | `PokemonResponseDTO` |
| **PATCH** | `/api/v1/pokemon/{id}` | Partially update specific fields of a Pokemon. | `PokemonRequestPatchDTO` | `PokemonResponseDTO` |
| **DELETE** | `/api/v1/pokemon/{id}` | Delete a Pokemon from the database. | *None* | `PokemonResponseDeleteDTO` |

### 💡 Notes on Request Validation
Endpoints using `POST` and `PUT` require a valid JSON body defined by `PokemonRequestDTO`. If any constraints (like fields being null or empty) are violated, the API will reject the request with a standard validation error response.

---

## 🐳 Useful Docker Compose Commands

*   **Stop the application:**
    ```bash
    docker-compose down
    ```
*   **Stop and delete volumes (wipes database data):**
    ```bash
    docker-compose down -v
    ```
*   **View real-time logs:**
    ```bash
    docker-compose logs -f
    ```

---

## 🔒 CORS Policy Configuration
This API is configured with `@CrossOrigin(origins = "*")` during development to allow requests from the React Frontend (`http://localhost:3000` or `http://localhost:5173`). Update this setting in production to restrict access to trusted origins only.
