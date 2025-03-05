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

### **2️⃣ Install Docker Compose**
Docker Compose is required to manage the database container.
- To verify installation, run:
  ```sh
  docker-compose --version
