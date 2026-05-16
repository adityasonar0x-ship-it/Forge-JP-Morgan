# Midas

# JPMorgan Chase Advanced Software Engineering Virtual Experience

Completed the JPMorgan Chase & Co. Advanced Software Engineering Virtual Experience Program on Forage.

## Project Overview
Midas Core is a Spring Boot backend application that processes financial transactions using Kafka, validates them, stores valid transactions in an H2 database, integrates with an external Incentives API, and exposes a REST API for querying user balances.

## Technologies Used
- Java 17
- Spring Boot
- Apache Kafka
- Maven
- H2 Database
- JPA / Hibernate
- REST API
- JUnit
- Testcontainers

## Tasks Completed

### Task 1: Project Setup
- Forked and cloned the project repository
- Configured Java 17
- Added required Maven dependencies
- Updated application configuration

### Task 2: Kafka Integration
- Implemented a Kafka listener
- Consumed incoming transaction messages
- Used the configured Kafka topic from `application.yml`

### Task 3: H2 Database Integration
- Integrated Midas Core with an H2 database
- Created transaction persistence logic
- Validated transactions before saving
- Updated sender and recipient balances

### Task 4: Incentives API Integration
- Connected Midas Core with the external Incentives API
- Posted validated transactions to the API
- Stored incentive amounts with transactions
- Added incentives to recipient balances

### Task 5: REST API for Balance Query
- Created a `/balance` GET endpoint
- Returned user balance using `userId`
- Returned balance `0` for invalid users
- Configured application to run on port `33400`

## Skills Demonstrated
- Backend development
- Event-driven architecture
- Kafka consumer integration
- Database design using JPA
- REST API development
- Debugging with test cases
- Spring Boot application configuration