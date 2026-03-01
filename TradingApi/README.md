# Trading API – Trading Journal Backend

A Spring Boot REST API for managing and analyzing trading journal data.

This project is being built in structured development phases to simulate real-world backend engineering practices — including validation, performance calculations, pagination, filtering, and security (coming next).

---

## 🚀 Project Overview

The Trading API is a backend service designed to help traders:

- Record trades
- Validate trade logic
- Calculate profit/loss automatically
- Track risk/reward ratios
- Analyze performance over time
- Retrieve paginated and filtered trade data

This project is intentionally built step-by-step to reflect how scalable backend systems evolve.

---

## 🛠 Tech Stack

- Java 21
- Spring Boot 4.0.3
- Spring Data JPA
- PostgreSQL
- Lombok
- Spring Validation
- Maven

---

## 📁 Project Structure

controller/
service/
repository/
entity/
dto/
exception/
config/
util/
Layered Architecture:

Controller → Service → Repository → Database

---

## ✔️ Implemented Features (Current State)

### 🧱 Core Infrastructure
- Spring Boot project initialized
- PostgreSQL database connected
- JPA & Hibernate configured
- Clean layered architecture
- .gitignore configured

---

### 👤 User Management
- Create User
- Fetch User
- Update User
- Delete User
- One-to-many relationship with trades

---

### 📊 Trade Engine (Business Logic)

- Create Trade (BUY / SELL)
- Update Trade
- Delete Trade
- Fetch all trades
- Pagination support
- Sorting support
- Whitelisted sorting fields
- Maximum page size enforcement

---

### 🧠 Trade Validation Rules

- All trade fields required (no open trades — trades are closed by default)
- Lot size must be positive
- Stop-loss must follow BUY/SELL rules
- Risk cannot be zero
- Supports both profitable and losing trades
- Throws custom TradeValidationException for invalid inputs

---

### 📈 Automatic Calculations

The system automatically calculates:

- Profit/Loss
- Risk amount
- Reward amount
- Risk-to-Reward ratio
- Win/Loss classification

Loss trades are fully supported.

---

### 📊 Performance Summary Endpoint

Provides aggregated statistics:

- Total trades
- Total wins
- Total losses
- Win rate
- Total profit
- Total loss
- Net profit
- Average risk/reward ratio

---

### 📄 Pagination & Sorting

- Pageable implemented via Spring Data
- Max page size protection
- Controlled sorting fields
- Safe API design to prevent abuse

Example:

GET /api/trades/user/{id}?page=0&size=10&sort=tradeDate,desc
---

## ⚙️ Prerequisites

- Java 21
- PostgreSQL running
- Maven
- Git

---

## 🗄 Database Setup

Create the database:

CREATE DATABASE trading_journal_db;
---

## 🔧 application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/trading_journal_db
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
---

## ▶️ How to Run Locally

1. Clone repository

   git clone https://github.com/Jaysilth/TradingApi.git

2. Navigate into folder

   cd TradingApi

3. Build project

   mvn clean install

4. Run application

   mvn spring-boot:run

App runs on:

http://localhost:8080
---

## 🌐 Example Endpoints

### Create Trade
POST /api/trades/user/{userId}
### Get Paginated Trades
GET /api/trades/user/{userId}?page=0&size=10&sort=tradeDate,desc
### Get Performance Summary
GET /api/trades/user/{userId}/summary
---

## 📌 Development Roadmap

### ✅ Completed
- Core backend structure
- Trade validation engine
- Profit/loss calculations
- Loss trade support
- Pagination & sorting
- Performance summary
- Custom exceptions
- Clean architecture layering

---

### 🚀 Next Phases
- Advanced filtering (symbol, date range, result type)
- JWT authentication & authorization
- Global exception handling (@ControllerAdvice)
- Analytics dashboard endpoints
- Swagger documentation
- Unit & integration tests
- Docker support
- Frontend integration

---

## 📚 Learning Goals

This project demonstrates:

- Real-world backend architecture
- Business logic enforcement in service layer
- API scalability considerations
- Defensive programming
- Trade performance analytics logic
- Clean and maintainable code structure

---

## 🤝 Contributing

Contributions are welcome.
Fork the repository and submit pull requests.

---

## 📄 License

This project is built for educational and professional portfolio purposes.

---

## 👤 Author

John Onadipe  
Backend Developer (Java & Spring Boot)

GitHub: https://github.com/Jaysilth