# JPMorgan Midas Core Banking System (Forage)

Backend transaction-processing system built as part of the JPMorgan Chase & Co. Advanced Software Engineering Virtual Experience Program on Forage.

## Features

- Kafka-based transaction ingestion
- Transaction validation and balance verification
- H2 in-memory database integration
- JPA/Hibernate persistence layer
- Incentive API integration using RestTemplate
- REST API for querying user balances
- Event-driven backend workflow using Spring Boot

## Tech Stack

- Java
- Spring Boot
- Apache Kafka
- Spring Data JPA
- Hibernate
- H2 Database
- Maven
- REST APIs

## Architecture Overview

Transaction Flow:

1. Incoming transactions are consumed through Kafka
2. Transactions are validated:
   - sender exists
   - recipient exists
   - sufficient sender balance
3. Valid transactions are persisted to the database
4. Incentive API is called for reward calculation
5. User balances are updated
6. REST endpoint exposes account balances

## REST Endpoint

### Get User Balance

```http
GET /balance?userId=1
```

Example Response:

```json
{
  "amount": 1326.98
}
```

## What I Learned

- Event-driven backend architecture
- Kafka consumer integration
- REST API development with Spring Boot
- Database persistence with JPA/Hibernate
- External API integration
- Transaction validation workflows
