// src/pages/Book.tsx
import React from "react";
import { Link } from "react-router-dom";
import "./book.css";

const Book: React.FC = () => {
  return (
    <div className="book-container">
      {/* Sidebar Navigation */}
      <div className="book-sidebar">
        <nav>
          <ul>
            <li>
              <Link to="/books/olympaid" className="sidebar-link">
                Olympaid
              </Link>
            </li>
            <li>
              <Link to="/books/history" className="sidebar-link">
                History
              </Link>
            </li>
            <li>
              <Link to="/books/general" className="sidebar-link">
                General
              </Link>
            </li>
          </ul>
        </nav>
      </div>

      {/* Main Content */}
      <div className="book-content">
        <h1>Welcome to OCMSE</h1>
        <p>
          OCMSE is dedicated to fostering knowledge, empowering individuals, and
          connecting communities through our expansive library, educational
          initiatives, and dynamic events.
        </p>
        <p>
          Our mission is to create an inclusive platform for learning,
          collaboration, and exploration of ideas.
        </p>

        {/* Sections */}
        <div className="book-section">
          <h2>Olympaids</h2>
          <p>
            The Open Collaborative Model for Scholarly Endeavors (OCMSE) is a
            platform aimed at revolutionizing how educational resources are
            created and shared. With a commitment to fostering collaboration and
            accessibility, OCMSE strives to bridge the gap between knowledge and
            the people who need it.
          </p>
          <Link to="/books/olympaid" className="section-link">
            Explore more books in olympaids
          </Link>
        </div>

        <div className="book-section">
          <h2>History</h2>
          <p>
            Our esteemed board of trustees comprises visionaries and leaders
            dedicated to steering OCMSE towards its mission. Their expertise and
            commitment ensure that the platform remains true to its values of
            innovation, collaboration, and inclusivity.
          </p>
          <Link to="/books/history" className="section-link">
            Explore more books in History
          </Link>
        </div>

        <div className="book-section">
          <h2>General</h2>
          <p>
            The advisory board brings together a diverse group of experts who
            provide strategic guidance and innovative ideas. Their collective
            wisdom and varied perspectives are instrumental in shaping OCMSE’s
            future.
          </p>
          <Link to="/books/general" className="section-link">
            Explore more books in General
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Book;
