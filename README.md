# Midas
Project repo for the JPMC Advanced Software Engineering Forage program

# JPMorgan Chase & Co. – Midas Core Virtual Internship Project

## Overview

This project was completed as part of the **JPMorgan Chase Software Engineering Virtual Experience Program**.
The objective of the simulation was to build and extend **Midas Core**, a backend financial transaction processing service using **Spring Boot**, **Kafka**, and **H2 Database**.

The system processes incoming financial transactions, validates them, applies incentives through an external API, updates user balances, and exposes a REST API to query balances.

All provided tests (**TaskOneTests → TaskFiveTests**) were successfully implemented and passed.

---

# Tech Stack

* Java
* Spring Boot
* Apache Kafka
* H2 In-Memory Database
* REST APIs
* Maven
* JUnit Testing

---

# System Architecture

The system consists of the following major components:

```
Transaction Producer (Kafka)
        │
        ▼
Kafka Listener (Midas Core)
        │
        ▼
Transaction Validation Service
        │
        ▼
H2 Database (Store Transactions & Users)
        │
        ▼
Incentive API Integration
        │
        ▼
Balance Update Logic
        │
        ▼
REST API (/balance)
```

---

# Features Implemented

## 1. Transaction Processing via Kafka

* Implemented a **Kafka Listener** that receives transaction events.
* Each transaction contains:

  * senderId
  * recipientId
  * amount
* The listener processes transactions asynchronously.

### Validation Rules

A transaction is considered valid when:

* Sender exists
* Recipient exists
* Sender has sufficient balance

Invalid transactions are ignored.

---

# 2. H2 Database Integration

The project integrates an **H2 in-memory database** to persist:

* Users
* Transactions
* Account balances

### Configuration

The H2 database is configured using Spring Boot properties and runs locally during application execution.

Key benefits:

* Fast in-memory operations
* Easy testing
* No external DB setup required

---

# 3. Incentive API Integration

An external **Incentive API service** is used to calculate transaction incentives.

### Implementation

* After validating a transaction, the system:

  1. Sends the transaction to the `/incentive` endpoint.
  2. Receives an **Incentive response**.
  3. Applies the incentive to the **recipient's balance**.

### Important Rule

* Incentives are **added only to the recipient**
* They are **NOT deducted from the sender**

---

# 4. Balance Update Logic

When a valid transaction occurs:

```
Sender Balance     = Sender Balance - Transaction Amount
Recipient Balance  = Recipient Balance + Transaction Amount + Incentive
```

This logic ensures correct financial accounting.

---

# 5. REST API for Balance Query

A REST API endpoint was implemented to allow balance queries.

### Endpoint

```
GET /balance
```

### Request Parameter

```
userId
```

### Example Request

```
http://localhost:33400/balance?userId=1
```

### Example Response

```
{
  "balance": 500
}
```

### Edge Case Handling

If the user does not exist:

```
{
  "balance": 0
}
```

---

# Server Configuration

The application runs on the required port:

```
server.port=33400
```

This allows the REST API to run alongside the Kafka listener.

---

# Project Structure

```
src/main/java
│
├── controller
│     └── BalanceController.java
│
├── service
│     └── TransactionService.java
│
├── repository
│     └── UserRepository.java
│
├── kafka
│     └── TransactionListener.java
│
├── model
│     ├── Transaction.java
│     ├── User.java
│     └── Balance.java
│
└── MidasCoreApplication.java
```

---

# Test Cases Completed

The project included multiple automated tests that validate system behavior.

### Task 1

Basic project setup and core structure.

### Task 2

Kafka transaction listener implementation.

### Task 3

Database integration and transaction validation logic.

### Task 4

Incentive API integration and balance adjustment.

### Task 5

REST API implementation to query user balances.

All tests were executed successfully using the provided test suite.

---

# Running the Project

### Step 1

Start the Incentive API service.

### Step 2

Run the Spring Boot application.

```
mvn spring-boot:run
```

### Step 3

Execute the tests.

```
TaskOneTests
TaskTwoTests
TaskThreeTests
TaskFourTests
TaskFiveTests
```

### Step 4

Verify balance API.

```
GET http://localhost:33400/balance?userId=<id>
```

---

# Key Learnings

This simulation helped develop practical skills in:

* Event-driven architecture
* Kafka-based messaging systems
* Spring Boot REST API development
* External API integration
* Database persistence
* Financial transaction handling

It also provided exposure to **real-world backend system design similar to those used in financial institutions**.

---

# Acknowledgment

This project was completed as part of the **JPMorgan Chase & Co. Software Engineering Virtual Experience Program**, which provides hands-on exposure to real-world financial technology systems and backend architecture.

---
