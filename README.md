# 🚗 Car Rental Shop Application

## 📖 Overview
The **Car Rental Shop Application** is a **Spring Boot-based backend service** designed to streamline car rental operations. It provides a backend system that handles:
- **User authentication** using JWT-based security.
- **Car management** for listing, retrieving, and managing car data.
- **Rental transactions** to book and track car rentals.

The application ensures **secure and efficient rental management** through **role-based access control** and **RESTful API design**.

---

## 🎯 Motivation
The goal of this project is to **simplify car rental management** by offering:
- A **centralized platform** for managing rentals, users, and cars.
- **Role-based authentication** to differentiate between users and admins.
- A **structured relational database** for handling rental records efficiently.
- **Secure transactions** with JWT-based authentication and access control.

### **Main Goals**
✔️ Automating the car rental process with a **structured digital solution**.  
✔️ Reducing reliance on **manual record-keeping**.  
✔️ Ensuring **scalability and performance** with modern backend technologies.  

---

## 🛠️ Technology Stack
This project leverages **modern technologies** to ensure **reliability, scalability, and security**.

### **Backend**
- **Spring Boot** – Main framework for backend development.
- **Spring Security & JWT** – Used for authentication and role-based access control.
- **Spring Data JPA & Hibernate** – ORM framework for database interactions.

### **Database**
- **PostgreSQL** – Relational database management system.
- **Flyway** – Database versioning and migrations.

### **Build & Dependency Management**
- **Maven** – Dependency management and project build tool.

### **Testing & Code Quality**
- **JUnit** – Unit testing framework.
- **AssertJ** – Fluent assertions for improved test readability.
- **JaCoCo** – Code coverage analysis.
- **SonarQube** – Code quality monitoring.

---

## 🔒 Security
The application follows best **security practices**:
- **JWT-based authentication** – Secure token-based authentication for users.
- **Role-based access control (RBAC)** – Different permissions for **users** and **admins**.
- **Stateless session management** – Ensures scalability by avoiding server-side session storage.
- **CSRF protection disabled** – Since it is a REST API intended for client-server communication.

---

## 📊 Database Schema
The application database consists of the following tables:

1️⃣ **Roles** – Stores role-based access control data.  
2️⃣ **Users** – Stores user information, including authentication details.  
3️⃣ **Cars** – Stores car details (make, model, year, price, etc.).  
4️⃣ **Rentals** – Stores rental transaction details.  

These tables define relationships between **users, roles, cars, and rental transactions**.

---

# 🚀 Startup Guide

## 📌 Prerequisites
Before running the application, ensure that you have the following installed:

### **1️⃣ Install Docker**
The application uses **PostgreSQL** as its database inside a **Docker container**.  
🔗 [Download Docker](https://www.docker.com/get-started)
## 3️⃣ Install Java & Maven

The backend application requires **Java 17+** and **Maven**.

### Install Java
- Install **JDK 17+**:
  - 🔗 [Download JDK](https://adoptium.net/)
- Verify installation:
  ```sh
  java -version
### **2️⃣ Install Docker Compose**
Docker Compose is required to manage the database container.
- To verify installation, run:
  ```sh
  docker-compose --version

SOME TEXT


## Step-by-Step Guide to Start the Application

### 1. Clone the Repository
First, clone the project repository from your source control (GitHub, GitLab, etc.):

```sh
git clone https://github.com/your-repository/car-rental-shop.git
cd car-rental-shop
```

## Start the Database with Docker
The project includes a `docker-compose.yml` file that sets up the PostgreSQL database inside a Docker container.

### Steps:
1. Navigate to the project directory.
2. Run the following command to start the database container:

   ```sh
   docker-compose up -d
   ```
   This will:
   - Pull the PostgreSQL image (if not already downloaded).
   - Create and start a database container with the required configuration.
   - Run the database in detached mode (`-d` flag) in the background.

3. Verify that the container is running:
   
   ```sh
   docker ps
   ```
   You should see a running PostgreSQL container.

## Configure Database Connection
The application uses an `application.properties` file to define database connection settings. Ensure that the connection details match the database container configuration.

The default settings in `src/main/resources/application.properties` should be:

```ini
spring.datasource.url=jdbc:postgresql://localhost:5432/carrent_db
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database=POSTGRESQL
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
```

If necessary, update the username and password to match the credentials defined in your `docker-compose.yml` file.

## Build the Application
Once the database is running, you need to build the project using Maven.

Run the following command inside the project root directory:

```sh
mvn clean install
```

This will:
- Download all dependencies.
- Compile the Java source code.
- Run tests (optional).
- Package the application into a `.jar` file.

## Start the Backend Application
After building the project, start the backend service using:

```sh
mvn spring-boot:run
```

or run the compiled JAR file manually:

```sh
java -jar target/car-rental-shop.jar
```
