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

🌐 Test the API

Once running, the app starts on:http://localhost:8080

Test endpoint example:GET /hello

Response:Trading API is active

🧩 Planned Features (Next Phases)

User authentication & authorization (Spring Security + JWT)

Trade CRUD operations (Create, Read, Update, Delete)

Trade performance analytics

Risk management metrics

REST API documentation (Swagger / OpenAPI)

DTO validation

Global exception handling

Unit & integration testing

Docker support

📌 Learning Goals

This project is built to practice and demonstrate:

Spring Boot best practices

RESTful API design

Database integration with JPA

Layered architecture

Clean code principles

Real-world backend project structure

🤝 Contributing

Contributions are welcome.
Feel free to fork the repository and submit pull requests.

📄 License

This project is for educational and personal learning purposes.

👤 Author

John Onadipe
Backend Developer (Java & Spring Boot)

GitHub: https://github.com/Jaysilth