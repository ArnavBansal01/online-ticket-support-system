Online Ticket Support System
8-Week Development Roadmap (1 Module Per Week)
Tech Stack

Frontend: React 18 + Tailwind CSS + Axios + React Router + Chart.js
Backend: Spring Boot 3 + Spring Security (JWT) + Spring Data JPA
Database: PostgreSQL 15
Realtime: WebSocket (STOMP)
Tools: Maven, Postman, Docker (optional)
Deployment: Heroku / Netlify

WEEK 1: User Authentication & Role Management
Goals

✅ Secure login and registration
✅ Role-based dashboards (User / Agent / Admin)
✅ JWT protected routes

Backend Tasks (Day 1–3)

Setup Spring Boot project (Maven)

Create users table:

CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR UNIQUE,
  password_hash VARCHAR,
  role VARCHAR(20),
  created_at TIMESTAMP
);


Configure Spring Security with JWT

Endpoints:

POST /api/auth/register
POST /api/auth/login
GET  /api/profile

Frontend Tasks (Day 4–6)

Create React app

Install Tailwind CSS, Axios, React Router

Pages: /login, /register, /profile

Store JWT in localStorage

Implement protected routes

Day 7: Demo

Test user registration → login → role-based dashboard

WEEK 2: Ticket Creation & Categorization
Goals

✅ Users create support tickets
✅ Ticket categorization and priority
✅ Ticket status lifecycle

Backend Tasks (Day 1–3)

Create tickets table:

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


Endpoints:

POST /api/tickets
GET  /api/tickets/my

Frontend Tasks (Day 4–6)

Pages: /create-ticket, /my-tickets

Ticket form with category and priority dropdown

Status badges (OPEN, IN_PROGRESS, RESOLVED)

Day 7: Demo

User creates ticket → views ticket list

WEEK 3: Agent Assignment & Ticket Workflow
Goals

✅ Admin assigns tickets to agents
✅ Agents manage assigned tickets
✅ Controlled status transitions

Backend Tasks (Day 1–3)

Create ticket_assignments table:

CREATE TABLE ticket_assignments (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES tickets,
  agent_id BIGINT REFERENCES users
);


Endpoints:

GET /api/agent/tickets
PUT /api/tickets/{id}/assign
PUT /api/tickets/{id}/status

Frontend Tasks (Day 4–6)

Pages: /agent/dashboard

Assigned tickets list

Status update dropdown

Day 7: Demo

Admin assigns ticket → Agent updates status

WEEK 4: Ticket Conversation System
Goals

✅ User–Agent communication
✅ Threaded ticket messages
✅ Message history

Backend Tasks (Day 1–3)

Create ticket_messages table:

CREATE TABLE ticket_messages (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES tickets,
  sender_id BIGINT REFERENCES users,
  message TEXT,
  timestamp TIMESTAMP
);


Endpoints:

POST /api/tickets/{id}/message
GET  /api/tickets/{id}/messages

Frontend Tasks (Day 4–6)

Ticket detail page with chat UI

Message bubbles for user and agent

Auto-scroll message view

Day 7: Demo

User sends message → Agent replies

WEEK 5: Real-Time Updates using WebSockets
Goals

✅ Live message updates
✅ Instant ticket status changes
✅ No page refresh

Backend Tasks (Day 1–4)

Configure WebSocket with STOMP

Topics:

/topic/ticket/{id}


Broadcast messages and status updates

Frontend Tasks (Day 5–6)

WebSocket client setup

Real-time message rendering

Toast notifications

Day 7: Demo

Agent replies → User receives instantly

WEEK 6: SLA Tracking & Ticket History
Goals

✅ SLA deadline tracking
✅ Overdue ticket identification
✅ Ticket history view

Backend Tasks (Day 1–3)

Add SLA fields to tickets:

sla_deadline, resolved_at


Endpoints:

GET /api/tickets/overdue
GET /api/tickets/history

Frontend Tasks (Day 4–6)

SLA countdown timers

Overdue ticket indicators

Ticket timeline view

Day 7: Demo

Ticket crosses SLA → marked overdue

WEEK 7: Admin Dashboard & Analytics
Goals

✅ System-wide analytics
✅ Agent performance tracking
✅ Resolution progress monitoring

Backend Tasks (Day 1–3)

Admin endpoints:

GET /api/admin/stats
GET /api/admin/agents/performance


Metrics:

Total tickets

Resolved tickets

Average resolution time

Frontend Tasks (Day 4–6)

Page: /admin/dashboard

Charts and progress bars

Agent workload table

Day 7: Demo

Admin views analytics and progress goals

WEEK 8: Feedback, Ratings & Final Demo
Goals

✅ User feedback after resolution
✅ Agent ratings system
✅ Project completion

Backend Tasks (Day 1–2)

Create feedback table:

CREATE TABLE feedback (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES tickets,
  rating INTEGER,
  comment TEXT
);


Endpoints:

POST /api/feedback
GET  /api/agents/{id}/rating

Frontend Tasks (Day 3–5)

Feedback form on resolved tickets

Star rating component

Agent rating display

Day 6–7: Final Demo

✅ Complete user → agent → resolution → feedback flow
✅ Bug fixes and documentation
✅ Final presentation and submission
