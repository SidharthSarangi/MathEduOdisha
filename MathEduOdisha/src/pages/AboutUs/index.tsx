// src/pages/AboutUsPage.tsx
import React from "react";
import { Link } from "react-router-dom";
import "./aboutUs.css";

const AboutUsPage: React.FC = () => {
  return (
    <div className="about-us-container">
      {/* Sidebar Navigation */}
      <div className="about-us-sidebar">
        <nav>
          <ul>
            <li>
              <Link to="/about-ocmse" className="sidebar-link">
                About OCMSE
              </Link>
            </li>
            <li>
              <Link to="/trustees" className="sidebar-link">
                Trustees
              </Link>
            </li>
            <li>
              <Link to="/advisory-board" className="sidebar-link">
                Advisory Board
              </Link>
            </li>
          </ul>
        </nav>
      </div>

      {/* Main Content */}
      <div className="about-us-content">
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
        <div className="about-us-section">
          <h2>About OCMSE</h2>
          <p>
            The Open Collaborative Model for Scholarly Endeavors (OCMSE) is a
            platform aimed at revolutionizing how educational resources are
            created and shared. With a commitment to fostering collaboration and
            accessibility, OCMSE strives to bridge the gap between knowledge and
            the people who need it.
          </p>
          <Link to="/about-ocmse" className="section-link">
            Learn More
          </Link>
        </div>

        <div className="about-us-section">
          <h2>Our Trustees</h2>
          <p>
            Our esteemed board of trustees comprises visionaries and leaders
            dedicated to steering OCMSE towards its mission. Their expertise and
            commitment ensure that the platform remains true to its values of
            innovation, collaboration, and inclusivity.
          </p>
          <Link to="/trustees" className="section-link">
            Meet Our Trustees
          </Link>
        </div>

        <div className="about-us-section">
          <h2>Advisory Board</h2>
          <p>
            The advisory board brings together a diverse group of experts who
            provide strategic guidance and innovative ideas. Their collective
            wisdom and varied perspectives are instrumental in shaping OCMSE’s
            future.
          </p>
          <Link to="/advisory-board" className="section-link">
            Learn About Our Advisory Board
          </Link>
        </div>
      </div>
    </div>
  );
};

export default AboutUsPage;
