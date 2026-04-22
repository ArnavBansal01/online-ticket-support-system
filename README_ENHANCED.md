# 🎟️ Online Ticket Support System

A robust, microservices-based support ticketing platform built with **React 18**, **Spring Boot 3**, and **JWT-based Security**. Designed for seamless interaction between Customers, Support Agents, and Administrators.

---

## 🚀 Quick Start Guide

### Prerequisites
- **Java 17**
- **Node.js 20+**
- **Maven 3.9+**
Failed to load resource: net::ERR_CONNECTION_REFUSED
:8081/api/auth/login:1  Failed to load resource: net::ERR_CONNECTION_REFUSED
:8081/api/auth/login:1  Failed to load resource: net::ERR_CONNECTION_REFUSED
:8081/api/auth/login:1  Failed to load resource: net::ERR_CONNECTION_REFUSED
:8081/api/auth/login:1  Failed to load resource: net::ERR_CONNECTION_REFUSED
:8081/api/auth/login:1  Failed to load resource: net::ERR_CONNECTION_REFUSED

### 1. Launch Backend Microservices
Open three separate terminals and run each service:

| Service | Directory | Command | Port |
| :--- | :--- | :--- | :--- |
| **User Service** | `backend/user-service` | `mvn spring-boot:run` | `8081` |
| **Ticket Service** | `backend/ticket-service` | `mvn spring-boot:run` | `8082` |
| **Analytics Service** | `backend/analytics-service` | `mvn spring-boot:run` | `8083` |

### 2. Launch Frontend
Open a fourth terminal in the root directory:
```bash
npm install
npm run dev
```
🌐 **URL**: `http://localhost:5173`

---

## 🔐 Login Credentials
The system comes pre-seeded with the following accounts for testing:

| Role | Email | Password |
| :--- | :--- | :--- |
| **👑 Administrator** | `admin@test.com` | `admin123` |
| **🛠️ Support Agent** | `agent@test.com` | `agent123` |
| **👤 Customer** | `user@test.com` | `user123` |

---

## ✨ Key Features
- **Secure Authentication**: Stateless JWT-based authentication with role-based access control.
- **Ticket Lifecycle**: Full management from creation to resolution and formal closure.
- **Real-time Analytics**: Dynamic dashboard for Admins featuring charts and performance metrics.
- **Internal Messaging**: Integrated communication thread within tickets for Agents and Customers.
- **Seamless Integration**: React frontend communicates across multiple specialized backends.

---

## 🧪 Comprehensive Testing Workflow

Follow this scenario to test every feature of the app:

### Phase 1: Customer Experience
1.  **Login** as Customer (`user@test.com`).
2.  **Raise a Ticket**: Click "Raise Ticket" and describe a mock issue (e.g., "Login issues").
3.  **Chat**: Open the ticket details and send the first message to support.

### Phase 2: Administrator Management
1.  **Login** as Admin (`admin@test.com`).
2.  **Assignment**: Go to the Admin Dashboard, find the new ticket, and assign it to the **Agent**.
3.  **Review**: Notice how the analytics charts update when a new ticket is registered.

### Phase 3: Agent Resolution
1.  **Login** as Agent (`agent@test.com`).
2.  **Workaround**: Find the ticket in your assigned list.
3.  **Messaging**: Respond to the customer's message.
4.  **Resolve**: Mark the ticket status as **RESOLVED** once the issue is fixed.

### Phase 4: Final Closure
1.  **Login back** as Customer.
2.  **Close Ticket**: Verify the resolution and click "Close Ticket" to finalize.
3.  **Review Dashboard**: Log in as Admin one last time to see the "Average Resolution Time" and "Closed Tickets" update.

---

## 🛠️ Tech Stack & Architecture
- **Frontend**: Vite, React, Axios, Lucide Icons, Recharts.
- **Backend**: Spring Boot 3, Spring JPA, Hibernate.
- **Security**: custom JWT Filter & JJWT library.
- **Database**: H2 (In-memory) - No manual database setup required.
- **Testing**: Handled via custom smoke-test scripts and REST controllers.

---

> [!NOTE]
> All services share a hardcoded JWT secret (`fake-secret-key-...`) for local development ease. Both the Ticket and Analytics services validate tokens issued by the User Service.
