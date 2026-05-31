# 🪙 JPMC Midas Core — Event-Driven Ledger & Incentive Engine

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-3.x-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)](https://kafka.apache.org/)
[![Hibernate](https://img.shields.io/badge/Hibernate-JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)](https://hibernate.org/)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![H2 Database](https://img.shields.io/badge/H2--Database-InMemory-007ACC?style=for-the-badge)](https://www.h2database.com/)

**Midas Core** is a robust, production-grade event-driven transaction ledger backend engineered as part of the **JPMorgan Chase Advanced Software Engineering** program. It processes large-scale, asynchronous transaction streams via Apache Kafka, computes real-time wallet balances, dynamically calculates cashbacks via an external REST microservice, and exposes high-throughput endpoints for instant account queries.

---

## 🏗️ System Architecture & Data Flow

Midas Core utilizes an event-driven microservices pattern with a highly optimized data-ingestion pipeline:

```text
       [ Kafka Transaction Stream ] (Topic: trader-updates)
                    │
                    ▼
     [ com.jpmc.midascore.component ]
          TransactionListener
                    │
         ┌──────────┴──────────┐
         ▼                     ▼
 [ Balance Verification ]  [ External API Request ] ────► [ Incentive API ]
 (Sender has funds?)       (RestTemplate POST)            (Port: 8080)
         │                     │                                 │
         │                     │◄────────────────────────────────┘
         ▼                     ▼
 [ Apply Transaction ]     [ Apply Cashbacks ] 
 (Deduct from sender)      (Add amount + incentive to recipient)
         │
         ▼
 [ Spring Data JPA ] ────► [ H2 In-Memory DB ]
 (TransactionRecord)
         ▲
         │
 [ Balance Controller ] ◄──── [ GET /balance?userId={id} ] (Port: 33400)
```

---

## ⚡ Core Features

* **Event-Driven Processing**: Consumes high-velocity transaction streams asynchronously via a dedicated Kafka listener.
* **Smart Wallet Ledger**: Validates user balances before processing, ensuring accounts never fall into negative balances.
* **REST Integration**: Dynamically interacts with an external Incentive API using HTTP POST to inject transaction cashbacks.
* **Microservices Ready**: Exposes clean JSON APIs with full cross-origin and routing support, running on port `33400`.
* **State Persistence**: Utilizes JPA/Hibernate with an In-Memory H2 DB for light-speed transactions and zero-latency state querying.

---

## 🚀 Tasks & Milestones Accomplished

### ✅ Tasks 1 & 2: Infrastructure Configuration
* Configured advanced consumer-producer settings in Spring Boot YAML to handle complex JSON deserialization safely.
* Setup automated DB populator logic to initialize users.

### ✅ Task 3: Kafka Broker Ingestion & Ledger Logic
* Developed `TransactionRecord` entity and persistence repository.
* Programmed fund verification algorithms to guarantee transactional integrity.
* **Milestone achieved:** Validated **Waldorf's** ledger transactions (ID 5) yielding a final balance of **627**.

### ✅ Task 4: Incentive REST Integration
* Connected Midas Core to the `/incentive` endpoint.
* Programmed custom cashback rules: recipients receive the additional incentive amount, while senders are only charged the principal transaction cost.
* **Milestone achieved:** Processed **Wilbur's** ledger transactions (ID 9) yielding a final balance of **3089**.

### ✅ Task 5: Web Layer API Exposure
* Designed and registered `BalanceController` exposing `GET /balance?userId=...` returning real-time `Balance` objects.
* Transitioned the entire app to run on port `33400` to avoid conflict with standard services.

---

## 🛠️ Installation & Getting Started

### 📋 Prerequisites
* Java JDK 17 or higher
* Apache Maven
* Local Git environment

### 🏃 How to Run the Project

1. **Clone & Navigate:**
   ```bash
   git clone https://github.com/vibhormishra1/forage-midas.git
   cd forage-midas
   ```

2. **Start the Incentive Microservice:**
   Launch the pre-built incentive engine JAR (runs on port `8080`):
   ```bash
   java -jar services/transaction-incentive-api.jar
   ```

3. **Start Midas Core:**
   Start the Spring Boot application (runs on port `33400`):
   ```bash
   mvn spring-boot:run
   ```

4. **Verify the Endpoint:**
   Check the balance of any user (e.g., Wilbur, ID `9`):
   ```bash
   curl "http://localhost:33400/balance?userId=9"
   ```
   **Expected JSON Response:**
   ```json
   {
     "userId": 9,
     "balance": 3089.42
   }
   ```

---

## 🧪 Running Tests
The pipeline comes with structured integration test suites that spin up embedded Kafka brokers and database instances automatically:

```bash
mvn clean test
```

* Test results for Tasks 3 and 4 are exported directly to local files in the root folder: `WALDORF_ANSWER.txt` and `WILBUR_ANSWER.txt`.

---

*Developed by Vibhor Mishra as part of the JPMorgan Chase Advanced Software Engineering Experience.*
