import React, { useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import FeaturedItem from "../../components/FeaturedItem";
import bannerPhoto from "../../assets/bannerPhoto.jpg";
import bookCover from "../../assets/bookCover.jpg";
import book from "../../assets/book.jpg";
import bookover from "../../assets/bookover.jpg";
import "./publication.css";
import { Col, Container, Row } from "react-bootstrap";

const PublicationPage: React.FC = () => {
  const sectionsRef = useRef<HTMLDivElement[]>([]);

  const featuredBooks = [
    {
      title: "Book 1",
      description: "An engaging book about...",
      imageUrl: bookCover,
      link: "/books/1",
    },
    {
      title: "Book 2",
      description: "An exciting adventure...",
      imageUrl: book,
      link: "/books/2",
    },
    {
      title: "Book 3",
      description: "A fascinating story...",
      imageUrl: bookover,
      link: "/books/3",
    },
  ];

  const featuredMagazines = [
    {
      title: "Magazine 1",
      description: "A popular magazine...",
      imageUrl: bannerPhoto,
      link: "/magazines/1",
    },
    {
      title: "Magazine 2",
      description: "The latest issue covers...",
      imageUrl: bannerPhoto,
      link: "/magazines/2",
    },
    {
      title: "Magazine 3",
      description: "Discover insights on...",
      imageUrl: bannerPhoto,
      link: "/magazines/3",
    },
  ];

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("visible");
          }
        });
      },
      { threshold: 0.1 } // Trigger when 10% of the section is visible
    );

    sectionsRef.current.forEach((section) => {
      observer.observe(section);
    });

    return () => {
      sectionsRef.current.forEach((section) => observer.unobserve(section));
    };
  }, []);

  return (
    <div className="publication-container">
      {/* Sidebar Navigation */}
      <div className="publication-sidebar">
        <nav>
          <ul>
            <li>
              <Link to="/books" className="sidebar-link">
                Books
              </Link>
            </li>
            <li>
              <Link to="/leelavati" className="sidebar-link">
                Trustees
              </Link>
            </li>
          </ul>
        </nav>
      </div>

      {/* Main Content */}
      <div className="publication-content">
        <h1>Publications</h1>
        <p>
          OCMSE is dedicated to fostering knowledge, empowering individuals, and
          connecting communities through our expansive library, educational
          initiatives, and dynamic events.
        </p>
        <p>
          Our mission is to create an inclusive platform for learning,
          collaboration, and exploration of ideas.
        </p>

        <div className="publication-section">
          <h2>Books</h2>
          <div
            className="fade-in"
            ref={(el) => el && sectionsRef.current.push(el)}
          >
            <Container className="mt-5">
              <h2 className="text-center mb-4">Featured Books</h2>
              <Row className="justify-content-center">
                {featuredBooks.map((book, index) => (
                  <Col key={index} xs={12} md={4}>
                    <FeaturedItem
                      title={book.title}
                      description={book.description}
                      imageUrl={book.imageUrl}
                      link={book.link}
                    />
                  </Col>
                ))}
              </Row>
            </Container>
          </div>
          <p>
            Our esteemed board of trustees comprises visionaries and leaders
            dedicated to steering OCMSE towards its mission. Their expertise and
            commitment ensure that the platform remains true to its values of
            innovation, collaboration, and inclusivity.
          </p>
          <Link to="/books" className="section-link">
            check more books
          </Link>
        </div>

        <div className="publication-section">
          <h2>Leelavati</h2>
          <div
            className="fade-in"
            ref={(el) => el && sectionsRef.current.push(el)}
          >
            <Container className="mt-5">
              <h2 className="text-center mb-4">Featured Magazines</h2>
              <Row className="justify-content-center">
                {featuredMagazines.map((magazine, index) => (
                  <Col key={index} xs={12} md={4}>
                    <FeaturedItem
                      title={magazine.title}
                      description={magazine.description}
                      imageUrl={magazine.imageUrl}
                      link={magazine.link}
                    />
                  </Col>
                ))}
              </Row>
            </Container>
          </div>
          <p>
            The advisory board brings together a diverse group of experts who
            provide strategic guidance and innovative ideas. Their collective
            wisdom and varied perspectives are instrumental in shaping OCMSE’s
            future.
          </p>
          <Link to="/leelavati" className="section-link">
            check more magazines
          </Link>
        </div>
      </div>
    </div>
  );
};

export default PublicationPage;
