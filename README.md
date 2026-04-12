# 🗓️ Appointment Scheduling System

## 📋 Project Overview

A comprehensive appointment scheduling system built with Java and Maven, implementing the Strategy and Observer design patterns.  
The system supports user authentication, appointment booking with business rules, notifications, and unit testing with JUnit and Mockito.

---

## ✨ Features

### 🔐 Authentication & Authorization
Separate login for administrators and regular users with different permission levels.

---

### 📅 Appointment Management
- View available time slots  
- Book appointments  
- Modify or cancel existing ones  
- Users can only manage their own appointments  
- Admins have full control  

---

### 📏 Flexible Booking Rules
Enforce:
- Duration limits  
- Participant caps  
- Type-specific rules using Strategy Pattern  

---

### 🧾 Multiple Appointment Types
- Individual  
- Group  
- Virtual  
- In-person  
- Urgent  
- Follow-up  
- Assessment  

---

### 🔔 Notification System
Automatic reminders and status updates sent via email and SMS using Observer Pattern (with mockable services for testing).

---

### 💾 In-Memory Persistence
Temporary storage using an in-memory repository for testing and development purposes.

---

### 📖 Fully Documented
All classes, methods, and fields include Javadoc comments.

---

### 🧪 Unit Testing
Built with JUnit 5 and Mockito, with JaCoCo coverage reports.

---

## 🛠️ Tech Stack

- Java 17  
- Maven  
- JUnit 5  
- Mockito  
- JaCoCo  
- OOP + Design Patterns  

---

## 🧠 Design Patterns

### Strategy Pattern
Used for booking rules (duration, participants, type validation)

### Observer Pattern
Used for notification system (Email / SMS / Mock notifications)

---

## 👨‍💻 Author

* Eman Khatatbeh & Shereen Hasan * 
