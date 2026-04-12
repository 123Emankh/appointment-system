- Appointment Scheduling System

## 📋 Project Overview

A comprehensive appointment scheduling system built with Java and Maven, implementing the Strategy and Observer design patterns. The system supports user authentication, appointment booking with business rules, notifications, and unit testing with JUnit and Mockito.

---

## ✨ Features

- Authentication & Authorization  
  Separate login for administrators and regular users with different permission levels.

- Appointment Management  
  View available time slots, book appointments, modify or cancel existing ones.  
  Users can only manage their own appointments, while admins have full control.

- Flexible Booking Rules  
  Enforce duration limits, participant caps, and type-specific rules using the Strategy pattern.

- Multiple Appointment Types  
  Supports:
  - Individual
  - Group
  - Virtual
  - In-person
  - Urgent
  - Follow-up
  - Assessment

- Notification System  
  Automatic reminders and status updates sent via email and SMS using Observer pattern (with mockable services for testing).

- In-Memory Persistence  
  Simple repository for testing and prototyping (can be replaced with a real database).

- Fully Documented  
  All classes, methods, and fields include Javadoc comments.

- Unit Testing  
  Built with JUnit 5 and Mockito, with JaCoCo coverage reports.

---

## 👨‍💻 Author

Eman Khatatbeh & shereen hasan  
