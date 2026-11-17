# Library Microservices System

This project containerizes four Spring Boot services:

| Service | Container port | Host port | Purpose |
| --- | ---: | ---: | --- |
| Eureka Server | 8761 | 8761 | Service discovery dashboard and registry |
| API Gateway | 8080 | 8080 | Single external entry point and route forwarding |
| Auth Service | 8081 | 8081 | Demo login endpoint |
| Book Service | 8082 | 8082 | Demo book catalog endpoints |

Each service has a multi-stage `Dockerfile`. The builder stage generates the service JAR with Maven and the runtime stage copies that generated JAR into a compact Java 21 runtime image.

## Start the system

Install Docker Desktop, then from the repository root run:

```powershell
docker compose build
docker compose up -d
docker compose ps
```

The compose network is named `library-network`. Containers resolve Eureka as `eureka-server`; the `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` environment variable makes every client register through Docker DNS rather than `localhost`.

## Use the services

Wait until the Eureka dashboard at [http://localhost:8761](http://localhost:8761) lists `API-GATEWAY`, `AUTH-SERVICE`, and `BOOK-SERVICE` as `UP`.

```powershell
# Through the gateway (recommended)
Invoke-RestMethod http://localhost:8080/books
Invoke-RestMethod -Method Post http://localhost:8080/auth/login `
  -ContentType 'application/json' `
  -Body '{"username":"reader","password":"library"}'

# Direct service ports, useful for development
Invoke-RestMethod http://localhost:8081/auth/health
Invoke-RestMethod http://localhost:8082/books/1
```

The login endpoint is deliberately a demo endpoint, not production authentication. Replace it with real credential storage, password hashing, JWT signing, TLS, and gateway authorization before deploying.

## Manage containers

```powershell
docker compose logs -f
docker compose down
```

To rebuild just one image after changing it, use for example:

```powershell
docker compose build book-service
docker compose up -d book-service
```

The explicit resulting tags are `library-eureka:1.0.0`, `library-api-gateway:1.0.0`, `library-auth-service:1.0.0`, and `library-book-service:1.0.0`.
