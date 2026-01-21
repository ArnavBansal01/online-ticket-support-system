# Online Ticket Support System

## Project Overview
The Online Ticket Support System is a role-based web application designed to manage customer support requests efficiently.  
Users can raise support tickets, agents can resolve them through real-time communication, and administrators can monitor performance using analytics and SLA tracking.

---

## Tech Stack

**Frontend**
- React 18
- Tailwind CSS
- Axios
- React Router
- Chart.js

**Backend**
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA

**Database**
- PostgreSQL 15

**Realtime & Tools**
- WebSocket (STOMP)
- Maven
- Postman
- Docker (Optional)

**Deployment**
- Heroku / Netlify

---

## 8-Week Development Roadmap

---

### Week 1: User Authentication & Role Management

#### Goals
- Secure user registration and login
- Role-based access (User, Agent, Admin)
- JWT protected APIs

#### Backend Tasks
- Setup Spring Boot project with Maven
- Create `users` table
```sql
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR UNIQUE,
  password_hash VARCHAR,
  role VARCHAR(20),
  created_at TIMESTAMP
);
Configure Spring Security with JWT

API Endpoints:

POST /api/auth/register
POST /api/auth/login
GET  /api/profile
Frontend Tasks
Create React application

Install Tailwind CSS, Axios, React Router

Pages: /login, /register, /profile

Store JWT in localStorage

Implement protected routes

Outcome
User registers → logs in → redirected to role-based dashboard

Week 2: Ticket Creation & Categorization
Goals
Users can create support tickets

Tickets categorized by priority and type

Ticket status management

Backend Tasks
Create tickets table

CREATE TABLE tickets (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT REFERENCES users,
  subject VARCHAR,
  description TEXT,
  category VARCHAR,
  priority VARCHAR,
  status VARCHAR,
  created_at TIMESTAMP
);
API Endpoints:

POST /api/tickets
GET  /api/tickets/my
Frontend Tasks
Pages: /create-ticket, /my-tickets

Ticket creation form with dropdowns

Ticket list with status badges

Outcome
User creates ticket → views ticket list

Week 3: Agent Assignment & Ticket Workflow
Goals
Admin assigns tickets to agents

Agents manage assigned tickets

Controlled status transitions

Backend Tasks
Create ticket_assignments table

CREATE TABLE ticket_assignments (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES tickets,
  agent_id BIGINT REFERENCES users
);
API Endpoints:

GET /api/agent/tickets
PUT /api/tickets/{id}/assign
PUT /api/tickets/{id}/status
Frontend Tasks
Page: /agent/dashboard

Assigned ticket list

Status update controls

Outcome
Admin assigns ticket → agent updates status

Week 4: Ticket Conversation System
Goals
User and agent communication

Threaded ticket discussions

Persistent message history

Backend Tasks
Create ticket_messages table

CREATE TABLE ticket_messages (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES tickets,
  sender_id BIGINT REFERENCES users,
  message TEXT,
  timestamp TIMESTAMP
);
API Endpoints:

POST /api/tickets/{id}/message
GET  /api/tickets/{id}/messages
Frontend Tasks
Ticket detail page with chat interface

Message bubbles (User / Agent)

Auto-scroll conversation view

Outcome
User sends message → agent replies

Week 5: Real-Time Updates using WebSockets
Goals
Live message updates

Instant ticket status notifications

No page refresh

Backend Tasks
Configure WebSocket with STOMP

Messaging topic:

/topic/ticket/{id}
Broadcast messages and status changes

Frontend Tasks
WebSocket client integration

Real-time message rendering

Toast notifications

Outcome
Agent reply appears instantly to user

Week 6: SLA Tracking & Ticket History
Goals
SLA deadline monitoring

Overdue ticket detection

Ticket resolution history

Backend Tasks
Add SLA fields to tickets

sla_deadline
resolved_at
API Endpoints:

GET /api/tickets/overdue
GET /api/tickets/history
Frontend Tasks
SLA countdown timers

Overdue ticket indicators

Ticket timeline view

Outcome
Tickets exceeding SLA marked automatically

Week 7: Admin Dashboard & Analytics
Goals
System analytics overview

Agent performance tracking

Resolution progress monitoring

Backend Tasks
Admin API Endpoints:

GET /api/admin/stats
GET /api/admin/agents/performance
Metrics:

Total tickets

Resolved tickets

Average resolution time

Frontend Tasks
Page: /admin/dashboard

Charts and progress bars

Agent workload table

Outcome
Admin monitors system performance visually

Week 8: Feedback, Ratings & Final Demo
Goals
User feedback after ticket resolution

Agent rating system

Project finalization

Backend Tasks
Create feedback table

CREATE TABLE feedback (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES tickets,
  rating INTEGER,
  comment TEXT
);
API Endpoints:

POST /api/feedback
GET  /api/agents/{id}/rating
Frontend Tasks
Feedback form for resolved tickets

Star rating component

Agent rating display

Outcome
User rates agent → admin views performance impact
