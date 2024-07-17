
# Vacation Management API

## Overview

The Vacation Management API is a Spring Boot application designed to manage employee vacation requests. It allows employees to submit vacation requests and managers to approve or reject them. The API includes endpoints for managing employees, vacation requests, and viewing remaining vacation days. The application also provides a Swagger UI for API documentation and testing.

## Built With

- Java 17
- Spring Boot 3.3.1
- Gradle
- H2 Database (for testing)
- Docker (for containerization)

## Features

- Employee Management
- Vacation Request Management
- Manager Approval Workflow
- Swagger UI for API documentation

## Getting Started

### Prerequisites

- Java 17
- Gradle
- Docker (optional, for containerized deployment)

### Running the Application

#### Using Gradle

1. **Clone the repository**: <br>
    CAUTION : The git url below was provided to the candidate, it maybe different for the audience on the other end.

    ```sh
    git clone git clone http://dexcom-1-jxdylk@git.codesubmit.io/dexcom-1/vacations-api-1-tedvgf
    cd vacations-api-1-tedvgf 
    ```

2. **Build the project**:

    ```sh
    ./gradlew build
    ```

3. **Run the application**:

    ```sh
    ./gradlew bootRun
    ```

4. **API Documentation**:

    - Swagger UI: `http://localhost:8080/swagger-ui/index.html`

#### Using Docker

1. **Build the Docker image**:

    ```sh
    docker build -t vacations-api-1-tedvgf:latest . 
   ```

2. **Run the Docker container**:

    ```sh
   docker run -p 8080:8080 vacations-api-1-tedvgf:latest .  
   ```

3. **Access the application**:

    - Swagger UI   : `http://localhost:8080/swagger-ui/index.html`
    -  Employee API : 
        - `http://localhost:8080/v1/api/employee/{employeeId}/requests`
        - `http://localhost:8080/v1/api/employee/{employeeId}/status/{status}`
        - `http://localhost:8080/v1/api/employee/{employeeId}/vacation-days-left`
        - `http://localhost:8080/v1/api/employee/vacation-requests`

   -  Manager API  : 
       - `http://localhost:8080/v1/api/managers/{managerId}/vacation-requests`
       - `http://localhost:8080/v1/api/managers/{managerId}/status/{status}`
       - `http://localhost:8080/v1/api/managers/{managerId}/employee/{employeeId}/overview`
       - `http://localhost:8080/v1/api/managers/{managerId}/vacation-requests/overlapping`
       - `http://localhost:8080/v1/api/managers/{managerId}/vacation-requests/{requestId}/process`

## API Documentation

The API documentation is available via Swagger UI. You can access it at `http://localhost:8080/swagger-ui/index.html`.

### Available Endpoints

#### Employee Endpoints

- `GET /v1/api/employee/{employeeId}/requests` - Get all vacation requests for an employee.
- `GET /v1/api/employee/{employeeId}/status/{status}` - Get vacation requests by status for an employee.
- `GET /v1/api/employee/{employeeId}/vacation-days-left` - Get remaining vacation days for an employee.
- `POST /v1/api/employee/vacation-requests` - Create a new vacation request.

#### Manager Endpoints

- `GET /v1/api/managers/{managerId}/vacation-requests` - Get all vacation requests for a manager.
- `GET /v1/api/managers/{managerId}/status/{status}` - Get vacation requests by status for a manager.
- `GET /v1/api/managers/{managerId}/employee/{employeeId}/overview` - Get vacation overview for an employee managed by a specific manager.
- `GET /v1/api/managers/{managerId}/vacation-requests/overlapping` - Get overlapping vacation requests for a manager.
- `POST /v1/api/managers/{managerId}/vacation-requests/{requestId}/process` - Process a vacation request (approve/reject).

## Acknowledgments

- Spring Boot documentation: https://spring.io/projects/spring-boot
- Swagger UI documentation: https://swagger.io/tools/swagger-ui/

---
