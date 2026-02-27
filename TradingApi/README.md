# Trading API – Trading Journal Backend (Phase 1)

A Spring Boot REST API for managing and analyzing trading journal data.  
This project is being built in phases, starting with core backend structure and database connectivity.

---

## 🚀 Project Overview

The Trading API is a backend service designed to help traders record, manage, and analyze their trades over time.

Phase 1 focuses on:
- Project setup
- Database connection
- Application structure
- Basic API testing

Future phases will include:
- Authentication & authorization (Spring Security)
- Trade CRUD operations
- Analytics & performance metrics
- Reporting & dashboards

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot 4.0.3
- Spring Data JPA
- PostgreSQL
- Lombok
- Spring Validation
- Maven

---

## 📁 Project Structure



---

## ✔️ Features (Phase 1)

- Spring Boot project initialized
- PostgreSQL database connection configured
- Application starts successfully with embedded Tomcat server
- Test endpoint created to verify API functionality
- JPA and Hibernate configured
- Clean layered architecture (Controller → Service → Repository)
- `.gitignore` configured to exclude build files and secrets

---

## ⚙️ Prerequisites

Before running the project, make sure you have:

- Java 21 installed
- PostgreSQL installed and running
- Maven installed
- Git installed

---

## 🗄️ Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE trading_journal_db;

```

---

## Update your application.properties
````
spring.datasource.url=jdbc:postgresql://localhost:5432/trading_journal_db
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

````
▶️ How to Run Locally

1. Clone the repository: git clone https://github.com/Jaysilth/TradingApi.git
2. Navigate into the project folder: cd TradingApi
3. Build the project: mvn clean install
4. Run the application: mvn spring-boot:run

## 🌐 Test the API

Once running, the app starts on:http://localhost:8080

Test endpoint example:GET /hello

Response:Trading API is active

## 📌 Project Milestones

### ✅ Completed
- Project Setup
    - Spring Boot with Maven, PostgreSQL, Lombok, Validation, DevTools
    - Project structured into controller, service, repository, entity, dto, exception, config, util
    - GitHub repository initialized

- Database & Entities
    - User entity (id, username, email, password, createdAt)
    - Trade entity (id, symbol, entryPrice, exitPrice, stopLoss, lotSize, tradeDirection, tradeDate, notes)
    - TradeDirection enum (BUY, SELL)
    - One-to-many and many-to-one relationships between User and Trade
    - Clean JSON responses using @JsonManagedReference / @JsonBackReference

- Repositories
    - UserRepo and TradeRepo with standard CRUD and custom queries

- Service Layer
    - TradeService with:
        - DTO validation
        - Entity mapping
        - Stop-loss and trading logic validation
        - Profit/Loss and Risk/Reward calculation
        - Custom exceptions (UserNotFoundException, TradeValidationException)

- DTOs
    - TradeRequestDto implemented

- Controllers
    - UserController and TradeController implemented
    - Endpoints tested with Postman for happy paths and exception cases

- Validation & Error Handling
    - Null checks for trade input
    - Stop-loss rules validated for BUY/SELL
    - Risk = 0 check implemented
    - Custom exceptions for user not found and invalid trades

- Testing
    - Verified happy paths: create user, create trade, fetch user/trades
    - Verified exception paths: invalid stop-loss, missing fields, non-existent users
    - Clean JSON output verified


- Trade CRUD
    - Update and Delete endpoints for trades and users



### ⚠️ Planned Features (Next Phases)
- User Security
    - Authentication & authorization (Spring Security + JWT)
    - Password hashing & secure login

- Validation Improvements
    - DTO validation using @Valid annotations for automated field checks

- Analytics & Reporting
    - Trade performance analytics
    - Risk/reward and profit/loss summaries
    - Filtering and search endpoints for trades

- API Documentation
    - Swagger / OpenAPI for interactive REST API docs

- Testing
    - Unit and integration tests for service and controller layers

- Deployment & DevOps
    - Docker support for containerization

- Frontend Integration
    - React / Vue / Angular frontend to consume the API

## 📌 Learning Goals

This project is built to practice and demonstrate:

Spring Boot best practices

RESTful API design

Database integration with JPA

Layered architecture

Clean code principles

Real-world backend project structure

## 🤝 Contributing

Contributions are welcome.
Feel free to fork the repository and submit pull requests.

## 📄 License

This project is for educational and personal learning purposes.
The comments were added with chatgpt to help boost understanding of why some coding decisions were made.
I was fully focused on building a scalable solution.

## 👤 Author

John Onadipe
Backend Developer (Java & Spring Boot)

GitHub: https://github.com/Jaysilth