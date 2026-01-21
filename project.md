# Online Ticket Support System

The **Online** Ticket Support System is a role-based full-stack web application that allows users to raise support tickets, agents to resolve them via real-time communication, and administrators to monitor performance using analytics and SLA tracking.[web:2][web:10] It streamlines customer support workflows with secure authentication, real-time messaging, SLA monitoring, and feedback-driven performance evaluation.[web:6][web:11]

---

## Features

- User registration and login with JWT-based authentication and role-based access control (User, Agent, Admin)[web:8][web:12][web:16]  
- Ticket creation, categorization by priority and type, and status management across the ticket lifecycle[web:6][web:11]  
- Admin-controlled agent assignment and ticket workflow (Open → In Progress → Resolved, etc.)[web:6][web:11]  
- Threaded ticket conversations between users and agents with persistent message history  
- Real-time ticket updates and notifications using WebSocket and STOMP[web:9][web:13]  
- SLA tracking, overdue ticket detection, and ticket history for auditing[web:2][web:6]  
- Admin dashboard with analytics and agent performance metrics using charts and tables[web:2][web:5][web:7]  
- Post-resolution feedback and agent rating system to measure support quality[web:6][web:11]

---

## Tech Stack

**Frontend**

- React 18  
- Tailwind CSS  
- Axios  
- React Router  
- Chart.js[web:7]

**Backend**

- Spring Boot 3  
- Spring Security (JWT)  
- Spring Data JPA[web:8][web:12]

**Database**

- PostgreSQL 15  

**Realtime & Tools**

- WebSocket (STOMP)[web:9][web:13]  
- Maven  
- Postman  
- Docker (optional)  

**Deployment**

- Heroku / Netlify[web:7]

---

## Core Database Schema

```sql
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR UNIQUE,
  password_hash VARCHAR,
  role VARCHAR(20),
  created_at TIMESTAMP
);

CREATE TABLE tickets (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT REFERENCES users,
  subject VARCHAR,
  description TEXT,
  category VARCHAR,
  priority VARCHAR,
  status VARCHAR,
  created_at TIMESTAMP,
  sla_deadline TIMESTAMP,
  resolved_at TIMESTAMP
);

CREATE TABLE ticket_assignments (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES tickets,
  agent_id BIGINT REFERENCES users
);

CREATE TABLE ticket_messages (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES tickets,
  sender_id BIGINT REFERENCES users,
  message TEXT,
  timestamp TIMESTAMP
);

CREATE TABLE feedback (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES tickets,
  rating INTEGER,
  comment TEXT
);


API Overview
Auth & Profile
text
POST /api/auth/register
POST /api/auth/login
GET  /api/profile
Tickets
text
POST /api/tickets
GET  /api/tickets/my
PUT  /api/tickets/{id}/assign
PUT  /api/tickets/{id}/status
GET  /api/tickets/overdue
GET  /api/tickets/history
Messages
text
POST /api/tickets/{id}/message
GET  /api/tickets/{id}/messages
Admin
text
GET /api/admin/stats
GET /api/admin/agents/performance
Feedback
text
POST /api/feedback
GET  /api/agents/{id}/rating
Real-Time Communication
WebSocket endpoint with STOMP for pushing real-time updates from server to clients.[web:9][web:13]

Topic pattern:

text
/topic/ticket/{id}
Used for:

Live ticket messages

Instant ticket status notifications

8-Week Development Roadmap
Week 1: User Authentication & Role Management
Goals

Secure user registration and login

Role-based access (User, Agent, Admin)

JWT-protected APIs[web:8][web:12][web:16]

Backend

Setup Spring Boot project with Maven

Implement users table and entities

Configure Spring Security + JWT

Implement /api/auth/register, /api/auth/login, /api/profile

Frontend

Initialize React app

Setup Tailwind CSS, Axios, React Router

Create /login, /register, /profile

Store JWT in localStorage and attach to Axios

Implement protected routes and role-based dashboards

Week 2: Ticket Creation & Categorization
Goals

Ticket creation by users

Categorization by priority and type

Basic ticket status management[web:6][web:11]

Backend

Implement tickets table and repository

Implement POST /api/tickets, GET /api/tickets/my

Frontend

/create-ticket, /my-tickets pages

Ticket form with subject, description, category, priority

Ticket list with status badges

Week 3: Agent Assignment & Ticket Workflow
Goals

Admin assigns tickets to agents

Agents manage assigned tickets

Controlled workflow transitions[web:6][web:11]

Backend

Implement ticket_assignments table

Implement:

GET /api/agent/tickets

PUT /api/tickets/{id}/assign

PUT /api/tickets/{id}/status

Frontend

/agent/dashboard page

Assigned ticket listing and status controls

Week 4: Ticket Conversation System
Goals

User–agent communication per ticket

Threaded conversations

Persistent message history[web:6][web:11]

Backend

Implement ticket_messages table

Implement:

POST /api/tickets/{id}/message

GET /api/tickets/{id}/messages

Frontend

Ticket detail page with chat interface

Message bubbles and auto-scroll

Week 5: Real-Time Updates using WebSockets
Goals

Live updates for messages

Instant ticket status notifications

No full page refresh[web:9][web:13]

Backend

Configure WebSocket + STOMP

Broadcast to /topic/ticket/{id}

Frontend

STOMP client integration

Subscribe per ticket

Real-time rendering and toast notifications

Week 6: SLA Tracking & Ticket History
Goals

SLA deadline monitoring

Overdue detection

Ticket history for audit[web:2][web:6]

Backend

Add sla_deadline, resolved_at to tickets

Implement GET /api/tickets/overdue, GET /api/tickets/history

Frontend

SLA countdown timers

Overdue indicators

Ticket timeline/history view

Week 7: Admin Dashboard & Analytics
Goals

System analytics overview

Agent performance tracking

SLA and resolution monitoring[web:2][web:5][web:7]

Backend

Implement GET /api/admin/stats, GET /api/admin/agents/performance

Frontend

/admin/dashboard with Chart.js graphs

Agent workload and performance table

Week 8: Feedback, Ratings & Final Demo
Goals

User feedback after resolution

Agent rating system

Final polish and demo[web:6][web:11]

Backend

Implement feedback table

Implement POST /api/feedback, GET /api/agents/{id}/rating

Frontend

Feedback form for resolved tickets

Star rating component and comment box

Display average ratings on admin/agent views
