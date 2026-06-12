# 🏥 EHR Backend

A robust Electronic Health Record (EHR) Management System backend built using Spring Boot. This application provides secure APIs for managing patient records, appointments, medical history, authentication, and healthcare-related operations.

## 🚀 Features

* Secure JWT-based Authentication & Authorization
* Role-Based Access Control (Admin, Doctor, Patient)
* Patient Record Management
* Appointment Scheduling
* Medical History Management
* RESTFul API Architecture
* Database Integration with JPA/Hibernate
* Exception Handling & Validation
* Scalable Layered Architecture

## 🛠️ Tech Stack

* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* Lombok

## 📂 Project Structure

```text
src
 ├── config
 ├── controller
 ├── entity
 ├── exception
 ├── model
 ├── repository
 ├── role
 ├── security
 ├── service
```

## ⚙️ Installation

### Clone Repository

```bash
git clone https://github.com/Vans-Co/EHR_Backend
cd EHR_Backend
```

### Configure Database

Update `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clinic
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Build Project

```bash
mvn clean install
```

### Run Application

```bash
mvn spring-boot:run
```

Application will start at:

```text
http://localhost:8086
```

## 🔐 Authentication

The application uses JWT (JSON Web Token) authentication.

### Authentication Flow

1. Register/Login
2. Receive JWT Token
3. Include token in request header

```http
Authorization: Bearer <your-token>
```

## 📌 API Modules

### Authentication

* User Registration
* User Login
* JWT Token Generation

### Patient Management

* Create Patient
* Update Patient
* View Patient Details
* Delete Patient

### Doctor Management

* Manage Doctor Profiles
* View Assigned Patients

### Appointment Management

* Book Appointment
* Update Appointment
* Cancel Appointment
* View Appointment History

### Medical Records

* Create Medical Record
* Update Medical Record
* View Medical History

## 🧪 Testing

Run tests using:

```bash
mvn test
```

## 📈 Future Enhancements

* Email Notifications
* Prescription Management
* Lab Report Integration
* File Upload Support
* Docker Deployment
* Microservices Architecture

## 👨‍💻 Author

**Naman Khairwar**

* Portfolio: https://v0-khairwarnaman.vercel.app/
* GitHub: https://github.com/Namankhairwar

