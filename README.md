# Online Ticket Support System

A full-stack support ticket platform built with a Vite + React frontend and three Spring Boot microservices. The app supports customer ticket creation, agent handling, admin oversight, and analytics dashboards with JWT-based authentication and role-based access control.

## Overview

The system is split into four parts:

- Frontend: React app with routing, dashboards, charts, and ticket workflows.
- User Service: authentication, registration, profile access, role management, and seeded demo users.
- Ticket Service: ticket creation, listing, details, assignment, status changes, closure, and soft delete.
- Analytics Service: summary cards, status and priority breakdowns, trends, customer stats, and agent performance.

## Tech Stack

- Frontend: React 19, React Router, Axios, Recharts, Vite
- Backend: Spring Boot 3, Spring Web, Spring Data JPA, Hibernate
- Database: H2 in-memory database per service
- Authentication: JWT shared across services for local development

## Project Structure

```text
.
|-- backend/
|   |-- user-service/
|   |-- ticket-service/
|   `-- analytics-service/
|-- src/
|   |-- api/
|   |-- auth/
|   |-- components/
|   |-- hooks/
|   |-- pages/
|   `-- utils/
|-- public/
|-- index.html
|-- package.json
`-- vite.config.js
```

## Features

- Customer registration and login
- Forgot password and public contact page
- Protected dashboard routes with role checks
- Ticket creation from the dashboard or contact form
- Ticket filtering by status, priority, date, category, and assignee
- Admin ticket assignment and bulk status updates
- Agent and customer ticket details view with status tracking
- Admin analytics with charts and performance metrics
- Session persistence with token validation on app start

## Prerequisites

- Node.js 20 or newer
- npm 10 or newer
- Java 17
- Maven 3.9 or newer

## Service Ports

- Frontend: 5173
- User Service: 8081
- Ticket Service: 8082
- Analytics Service: 8083

## Demo Accounts

The User Service seeds these accounts automatically on startup:

| Role | Email | Password |
| --- | --- | --- |
| ADMIN | admin@test.com | admin123 |
| AGENT | agent@test.com | agent123 |
| CUSTOMER | user@test.com | user123 |

## Local Setup

1. Install frontend dependencies from the project root:

```bash
npm install
```

2. Start the User Service:

```bash
cd backend/user-service
mvn spring-boot:run
```

3. Start the Ticket Service:

```bash
cd backend/ticket-service
mvn spring-boot:run
```

4. Start the Analytics Service:

```bash
cd backend/analytics-service
mvn spring-boot:run
```

5. Start the frontend in a separate terminal:

```bash
npm run dev
```

Open the app at http://localhost:5173.

## Common Routes

- / - home page
- /login - sign in
- /register - create an account
- /forgot-password - password recovery screen
- /contact - public contact form
- /dashboard - customer, agent, and admin dashboard
- /raise-ticket - ticket creation page
- /tickets/:id - ticket details page
- /admin - admin dashboard
- /403 - forbidden page

## API Summary

### User Service

- POST /api/auth/register
- POST /api/auth/login
- GET /api/auth/me
- GET /api/users
- GET /api/users/{id}
- PUT /api/users/{id}
- PUT /api/users/{id}/role
- DELETE /api/users/{id}

### Ticket Service

- POST /api/tickets
- GET /api/tickets
- GET /api/tickets/{id}
- PUT /api/tickets/{id}
- PUT /api/tickets/{id}/status
- PUT /api/tickets/{id}/assign
- PUT /api/tickets/{id}/close
- DELETE /api/tickets/{id}

### Analytics Service

- GET /api/analytics/summary
- GET /api/analytics/by-status
- GET /api/analytics/by-priority
- GET /api/analytics/by-category
- GET /api/analytics/trend?period=30d
- GET /api/analytics/agent-performance
- GET /api/analytics/customer-stats/{userId}

## Typical Workflow

1. Log in as a customer and create a ticket.
2. Log in as an admin and assign the ticket to an agent.
3. Log in as an agent and move the ticket through progress states.
4. Log back in as the customer and close the ticket after resolution.
5. Check the admin dashboard for updated analytics and status counts.

## Build and Validation

From the project root:

```bash
npm run lint
npm run build
```

Backend compile checks:

```bash
cd backend/user-service && mvn -DskipTests compile
cd backend/ticket-service && mvn -DskipTests compile
cd backend/analytics-service && mvn -DskipTests compile
```

## H2 Console

When the services are running, the H2 console is available at:

- http://localhost:8081/h2-console
- http://localhost:8082/h2-console

Use the JDBC URL defined in each service's application.yml file.

## Troubleshooting

- 401 Unauthorized: check that the backend services are running and the JWT token is present.
- 403 Forbidden: verify the current user role against the page or API you are trying to access.
- Frontend requests failing: confirm ports 8081, 8082, and 8083 are all running.
- Maven command not found: install Maven or invoke it using its full path.

## Security Note

This project uses a shared local JWT secret for development. Replace the hardcoded auth secret and related configuration before using the system in production.
