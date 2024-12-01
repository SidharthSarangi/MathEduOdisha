// src/components/AboutSection.tsx
import React from "react";
import "./aboutSection.css";

const AboutSection: React.FC = () => {
  return (
    <div className="about-section">
      <h1 className="welcome-heading">Welcome to OCMSE</h1>
      <p className="about-content">
        OCMSE (Online Community for Modern Scholarly Excellence) is dedicated to
        fostering a vibrant community of readers, writers, and contributors who
        share a passion for books, magazines, and impactful events. Our mission
        is to connect like-minded individuals, provide access to valuable
        resources, and celebrate creativity and knowledge.
      </p>
    </div>
  );
};

export default AboutSection;
