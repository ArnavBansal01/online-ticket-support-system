import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';

function Home() {
  useEffect(() => {
    function reveal() {
      const elements = document.querySelectorAll('.reveal');
      elements.forEach(el => {
        const windowHeight = window.innerHeight;
        const elementTop = el.getBoundingClientRect().top;
        if (elementTop < windowHeight - 100) {
          el.classList.add('active');
        }
      });
    }

    window.addEventListener('scroll', reveal);
    reveal(); // Call once on mount

    return () => window.removeEventListener('scroll', reveal);
  }, []);

  return (
    <>
      <Header />
      <section className="hero reveal">
        <div className="container">
          <h2>Simplifying Support. Resolving Issues Faster.</h2>
          <p>
            ResolveHub is a powerful online ticket support system that allows users
            to submit issues, track progress, and communicate efficiently with support agents.
          </p>
          <div className="hero-buttons">
            <Link to="/raise-ticket" className="btn-primary">Raise Ticket</Link>
            <Link to="/login" className="btn-outline">Login</Link>
          </div>
        </div>
      </section>

      <section className="section reveal">
        <div className="container">
          <h2>About ResolveHub</h2>
          <p>
            ResolveHub streamlines customer support through a structured ticket management system.
            Users can report issues, categorize them by priority, and track resolution status in real-time.
          </p>
          <ul>
            <li>Easy ticket submission</li>
            <li>Organized communication</li>
            <li>Real-time status tracking</li>
          </ul>
        </div>
      </section>

      <section className="section reveal">
        <div className="container">
          <h2>How It Works</h2>
          <div className="steps">
            <div className="card">
              <h3>Submit Your Ticket</h3>
              <p>Fill out a detailed form describing your issue and priority.</p>
            </div>
            <div className="card">
              <h3>Ticket Processing</h3>
              <p>Support agents review the issue and update its status.</p>
            </div>
            <div className="card">
              <h3>Track & Communicate</h3>
              <p>Monitor progress and communicate with support agents.</p>
            </div>
            <div className="card">
              <h3>Issue Resolution</h3>
              <p>Your issue is resolved and marked as closed.</p>
            </div>
          </div>
        </div>
      </section>

      <section className="section reveal">
        <div className="container">
          <h2>Key Features</h2>
          <div className="features">
            <div className="card">
              <h3>Secure Authentication</h3>
              <p>Safe login and role-based access control.</p>
            </div>
            <div className="card">
              <h3>Powerful Dashboard</h3>
              <p>Manage and track all tickets easily.</p>
            </div>
            <div className="card">
              <h3>Status Tracking</h3>
              <p>Open, In Progress, Closed status updates.</p>
            </div>
          </div>
        </div>
      </section>
      <Footer />
    </>
  );
}

export default Home;
