# Forage JPMC Advanced Software Engineering — Midas Core

A Spring Boot microservice built as part of the **JPMorgan Chase & Co. Advanced Software Engineering Virtual Experience** on Forage.

## Project Overview

Midas Core is a financial transaction processing system that consumes transactions from a Kafka topic, applies incentives via an external REST API, persists data to an H2 database, and exposes a REST API for querying user balances.

## Tasks Completed

### Task 1 — Project Setup ?
- Set up the Spring Boot project with Maven
- Configured dependencies for Kafka, JPA, H2, and Spring Web

### Task 2 — Kafka Integration ?
- Implemented a Kafka consumer (TransactionListener) to listen on the trader-updates topic
- Parsed incoming Transaction messages and processed them
- Answer submitted: 122.86, 42.87, 161.79, 22.22

### Task 3 — H2 Database + JPA ?
- Created UserRecord and TransactionRecord JPA entities
- Configured H2 in-memory database
- Implemented UserRepository and TransactionRepository
- Persisted all incoming transactions to the database
- Answer submitted: 627

### Task 4 — Incentive API Integration ?
- Implemented IncentiveService to POST transactions to the external Incentive API
- Applied returned incentive amounts to user balances
- Integrated incentive logic into DatabaseConduit
- Answer submitted: 3089

### Task 5 — Balance REST Endpoint ?
- Created BalanceController with a GET /balance endpoint
- Accepts userId as a request parameter
- Returns a JSON-serialized Balance object
- Returns balance of 0.0 for non-existent users
- Configured application to run on port 33400

## Tech Stack

- Java 17
- Spring Boot 3.2.5
- Apache Kafka
- Spring Data JPA
- H2 Database
- Spring Web
- Maven 3.9.15

## How to Run

### Step 1 - Start the Incentive API
java -jar services/transaction-incentive-api.jar

### Step 2 - Run the tests
mvn test -Dtest=TaskFiveTests

The application runs on port 33400.

### Balance Endpoint
GET http://localhost:33400/balance?userId=1

## Certificate
Completed the JPMorgan Chase & Co. Advanced Software Engineering Virtual Experience on Forage.
