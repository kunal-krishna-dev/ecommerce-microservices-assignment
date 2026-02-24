E-commerce Microservices
--------------------------
# Overview

This project implements a simple e-commerce backend using two Spring Boot microservices:

Inventory Service
Order Service

The Inventory Service is responsible for managing product stock and reserving items using a FEFO (First Expiry First Out) strategy.
The Order Service places orders and communicates with the Inventory Service to reserve stock before confirming the order.

# Tech Stack
Java 17
Spring Boot 3.x
Maven
H2 In-Memory Database
Liquibase (for schema & data migration)
JUnit 5
Mockito
MockMvc

# Project Structure
ecommerce-microservices-assignment/
	inventory-service/
	order-service/
	README.md

Each service is an independent Spring Boot application with its own database and configuration.

# How to Run the Project
1. Prerequisites

Java 17
Maven 3.x

2. Build the Project

From the root directory:
mvn clean install

3. Run Inventory Service
cd inventory-service
mvn spring-boot:run

Runs on: http://localhost:8081

4. Run Order Service
cd order-service
mvn spring-boot:run

Runs on: http://localhost:8080

5. Database & Data Loading

Both services use an in-memory H2 database.

Liquibase is configured to:
Create database schema
Load initial data

Run automatically at application startup

You can access the H2 console at:
http://localhost:8080/h2-console
http://localhost:8081/h2-console

# API Endpoints
1. Inventory Service

POST /inventory/reserve

Request:

{
  "productId": 1001,
  "quantity": 2
}

Successful Response:

{
  "productId": 1001,
  "productName": "Laptop",
  "batchIds": [1, 2]
}

2. Order Service

POST /order

Request:

{
  "productId": 1001,
  "quantity": 2
}

Successful Response (201 Created):

{
  "orderId": 1,
  "productId": 1001,
  "productName": "Laptop",
  "quantity": 2,
  "status": "PLACED",
  "reservedFromBatchIds": [1, 2],
  "message": "Order placed. Inventory reserved."
}

The Order Service internally calls the Inventory Service to reserve stock before confirming the order.

# Testing

The project includes both unit and integration tests.

1. Unit Tests

Service layer tested using Mockito
Repository layer mocked where required

2. Integration Tests

@SpringBootTest used for full context loading
H2 database for test environment
MockMvc used to test REST endpoints

# To run tests:

mvn clean test

# Design Decisions

FEFO strategy implemented using Strategy Pattern for extensibility.
Clear separation of layers: Controller → Service → Repository.
DTOs used to separate API models from persistence models.
Proper REST status codes used (e.g., 201 for order creation).
Liquibase used instead of manual SQL scripts to manage schema and data.