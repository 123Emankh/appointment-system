# -appointment-system
# * Appointment Scheduling System *

A comprehensive appointment scheduling system built with Java and Maven, implementing the Strategy and Observer design patterns. The system supports user authentication, appointment booking with business rules, notifications, and unit testing with JUnit and Mockito.

# * Features *

Authentication & Authorization: Separate login for administrators and regular users with different permission levels.

Appointment Management: View available time slots, book appointments, modify or cancel existing ones (users can only manage their own; admins have full control).

Flexible Booking Rules: Enforce duration limits, participant caps, and type-specific rules using the Strategy pattern.

Multiple Appointment Types: Support for individual, group, virtual, in-person, urgent, follow-up, and assessment appointments.

Notification System: Automatic reminders and status updates sent via email and SMS (with mockable services for testing) using the Observer pattern.

In-Memory Persistence: Simple repository implementation for easy testing and prototyping (can be replaced with a real database).

Fully Documented: All classes, methods, and fields include Javadoc comments.

Unit Testing: Comprehensive test suite using JUnit 5 and Mockito, with code coverage reports via JaCoCo.
