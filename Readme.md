# 🎫 Online Ticket Support System

A comprehensive full-stack web application for managing customer support tickets. This system streamlines communication between users and support agents through secure, threaded conversations and status tracking.



---

## 🛠 Technology Stack

| Domain | Technologies |
| :--- | :--- |
| **Frontend** | HTML5, CSS3 (Variables), JavaScript (ES6+), DOM API |
| **Backend** | Node.js, Express.js,Springboot |
| **Database** | MongoDB, Mongoose ODM |
| **Security** | JWT, Bcrypt, Dotenv |
| **DevOps** | Git, Vercel (Frontend), Render (Backend) |

---

## 🗺️ Implementation Roadmap (Modules)

This project was developed in 8 distinct modules, progressing from structural design to cloud deployment.

### 🔹 Phase 1: Foundation & UI Design

#### [Module 1] Project Initialization & Structure
* **Setup:** Git repository initialization and version control configuration.
* **Skeleton:** Constructed semantic HTML structure for Authentication, Dashboard, Ticket Forms, and Admin Panels.
* **✅ Deliverable:** A multi-page static website skeleton.

#### [Module 2] UI/UX Design & Responsiveness
* **Styling:** Implementation of CSS Variables for a consistent, professional color palette.
* **Components:** Designed reusable UI cards with dynamic status badges (Open/Pending/Closed).
* **Layout:** Mobile-first responsive grid system ensuring compatibility across devices.
* **✅ Deliverable:** A polished, responsive User Interface.

---

### 🔹 Phase 2: Core Logic & API

#### [Module 3] Frontend Logic & DOM Manipulation
* **Interactivity:** Converted static HTML to dynamic pages using Vanilla JavaScript.
* **Validation:** Client-side checks for email formats and password strength.
* **Simulation:** Dynamic rendering of ticket lists using dummy data to test UI states.
* **✅ Deliverable:** Interactive frontend prototype with input validation.

#### [Module 4] Backend API Development
* **Server:** Node.js and Express server configuration.
* **API Architecture:** RESTful endpoints created for:
    * `POST /api/auth` (Registration)
    * `GET /api/tickets` (Retrieval)
    * `POST /api/tickets` (Creation)
* **✅ Deliverable:** Functional backend server tested via Postman.

#### [Module 5] Database Integration
* **Connection:** Integrated MongoDB using Mongoose.
* **Schema Design:** relational modeling for **Users** and **Tickets**.
* **CRUD:** Refactored APIs to persist real data (Create, Read, Update, Delete).
* **✅ Deliverable:** Persistent data storage with proper relationships.

---

### 🔹 Phase 3: Security & Advanced Features

#### [Module 6] Authentication & Role-Based Access
* **Security:** Password encryption using **Bcrypt**.
* **Session:** **JWT (JSON Web Token)** implementation for stateless authentication.
* **Authorization:** Middleware logic to separate **Customer** (Own tickets only) vs **Support Agent** (Global view) access.
* **✅ Deliverable:** Secure, role-protected system.

#### [Module 7] Communication & Notifications
* **Threading:** Built a commenting system for Agents and Users to discuss specific tickets.
* **Notifications:** Integrated **Nodemailer** for email alerts on ticket replies.
* **UX Features:** Added search filters and status sorting.
* **✅ Deliverable:** Real-time conversation history and automated alerts.

---

### 🔹 Phase 4: Production

#### [Module 8] Optimization & Cloud Deployment
* **Security:** Environment variables (`.env`) configured for API keys and DB URIs.
* **Deployment:**
    * Frontend deployed to **Vercel/Netlify**.
    * Backend deployed to **Render/Railway**.
* **QA:** Final live testing and bug fixing.
* **✅ Final Deliverable:** Production-ready Live Link.

---

---

## 🏁 Conclusion

This project successfully integrates all 8 core modules, resulting in a secure, scalable, and fully functional Ticket Support System. It demonstrates proficiency in full-stack JavaScript development, database management, and cloud architecture.
