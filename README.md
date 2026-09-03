# Turfly Backend

A robust Spring Boot backend service for the Turfly application. Built with Java 17, this RESTful API provides secure authentication, data persistence, and business logic for the Turfly platform.

## Overview

Turfly Backend is a Spring Boot 4.0.9 application that serves as the core API for the Turfly application. It implements JWT-based authentication, MySQL database integration, and comprehensive validation to ensure data integrity and security.

## Technology Stack

- **Framework**: Spring Boot 4.0.9 (SNAPSHOT)
- **Language**: Java 17
- **Database**: MySQL
- **Build Tool**: Maven
- **Authentication**: JWT (JSON Web Tokens)
- **Security**: Spring Security
- **ORM**: Spring Data JPA
- **Validation**: Spring Validation
- **Server**: Embedded Tomcat (Spring WebMVC)
- **Containerization**: Docker

## Key Features

- 🔐 **JWT Authentication** - Secure token-based authentication using jjwt library (v0.12.6)
- 🔒 **Spring Security** - Comprehensive security framework for authorization and protection
- 📊 **Data Persistence** - MySQL integration with Spring Data JPA
- ✅ **Input Validation** - Built-in validation for request data
- 🧪 **Testing Support** - Comprehensive test dependencies including Mockito and Spring Test
- 🐳 **Docker Ready** - Multi-stage Docker build for optimized production deployment
- 📦 **Lombok Support** - Simplified Java code with annotations

## Project Structure

```
turfly/
├── src/
│   ├── main/
│   │   ├── java/              # Application source code
│   │   └── resources/         # Configuration files
│   └── test/
│       └── java/              # Test code
├── pom.xml                    # Maven configuration
├── mvnw / mvnw.cmd           # Maven Wrapper scripts
├── Dockerfile                 # Multi-stage Docker build
└── README.md                  # This file
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use the included Maven Wrapper)
- MySQL 8.0+
- Docker (optional, for containerized deployment)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Steve-B17/turfly.git
cd turfly
```

### 2. Configure Database

Update your `application.properties` or `application.yml` with your MySQL configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/turfly
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build the Project

Using Maven Wrapper (no installation required):

```bash
# On Linux/Mac
./mvnw clean install

# On Windows
mvnw.cmd clean install
```

Or with installed Maven:

```bash
mvn clean install
```

### 4. Run the Application

```bash
# Using Maven
./mvnw spring-boot:run

# Or run the JAR directly
java -jar target/turfly-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## Docker Deployment

### Build Docker Image

```bash
docker build -t turfly:latest .
```

### Run Docker Container

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-host:3306/turfly \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  turfly:latest
```

## API Endpoints

The API follows RESTful conventions. Detailed API documentation should be added to this README as endpoints are developed.

### Authentication

- POST `/api/auth/register` - Register new user
- POST `/api/auth/login` - User login (returns JWT token)

### Protected Endpoints

Include the JWT token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

## Dependencies

### Core Dependencies
- `spring-boot-starter-data-jpa` - JPA/Hibernate ORM
- `spring-boot-starter-security` - Security framework
- `spring-boot-starter-validation` - Bean validation
- `spring-boot-starter-webmvc` - Web/REST support
- `mysql-connector-j` - MySQL JDBC driver
- `jjwt` - JWT token generation and validation (v0.12.6)
- `lombok` - Boilerplate code reduction

### Test Dependencies
- `spring-boot-starter-test` - Spring Boot testing utilities
- `spring-boot-starter-security-test` - Security testing
- `spring-boot-starter-webmvc-test` - Web testing
- `mockito-core` - Mocking framework

For complete dependency details, see `pom.xml`

## Testing

Run tests with Maven:

```bash
./mvnw test
```

## Development

### Code Style

- Java 17 compatible syntax
- Use Lombok annotations to reduce boilerplate
- Follow Spring conventions for beans and components

### Adding Dependencies

Edit `pom.xml` and run:

```bash
./mvnw dependency:resolve
```

## Environment Variables

Key environment variables for configuration:

```bash
SPRING_DATASOURCE_URL         # Database connection URL
SPRING_DATASOURCE_USERNAME    # Database username
SPRING_DATASOURCE_PASSWORD    # Database password
SERVER_PORT                   # Server port (default: 8080)
JWT_SECRET                    # Secret key for JWT signing
```

## Configuration

Configuration can be managed through:

- `application.properties` - Property-based configuration
- `application.yml` - YAML-based configuration
- Environment variables - For containerized deployments

## Troubleshooting

### Maven Build Issues

```bash
# Clear Maven cache
./mvnw clean

# Update dependencies
./mvnw dependency:resolve
```

### Database Connection Issues

- Ensure MySQL is running and accessible
- Verify connection string in application properties
- Check database credentials

### Port Already in Use

Change the server port:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is part of the Turfly application ecosystem.

## Support

For issues, questions, or suggestions, please open an [GitHub Issue](https://github.com/Steve-B17/turfly/issues).

## Related Projects

- **Turfly Frontend** - The companion mobile/web application frontend

---

**Last Updated**: September 2026
