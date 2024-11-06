import React, { useEffect, useRef, useState } from "react";
import { Col, Container, Row, Spinner } from "react-bootstrap";
import { fetchEvents } from "../../api/events";
import { EventType as Event } from "../../types/eventType";
import EventBanner from "../../components/EventBanner";
import TestimonialsSection from "../../components/Testimonials";
import FeaturedItem from "../../components/FeaturedItem";
import bannerPhoto from "../../assets/bannerPhoto.jpg";
import bookCover from "../../assets/bookCover.jpg";
import book from "../../assets/book.jpg";
import bookover from "../../assets/bookover.jpg";
import TopDonations from "../../components/TopDonations";
import "./home.css";

const Home: React.FC = () => {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState<Boolean>(false);

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

  const testimonialsData = [
    {
      quote: "An amazing collection of books and knowledge.",
      author: "IISC",
    },
    { quote: "A must-visit for every book lover!", author: "John Smith" },
    {
      quote: "Incredible place to explore timeless literature.",
      author: "CMI",
    },
    {
      quote: "Explore the aura of maths.",
      author: "IITD",
    },
  ];

  useEffect(() => {
    // Fetch events from API
    const loadEvents = async () => {
      const data = await fetchEvents();
      // const currentAndUpcomingEvents = data.filter(
      //   (event) => new Date(event.date) >= new Date()
      // );
      setEvents(data);
      setLoading(false);
    };
    loadEvents();

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

  if (loading)
    return (
      <Spinner
        animation="border"
        variant="primary"
        className="d-block mx-auto mt-5"
      />
    );

  return (
    <Container fluid className="p-0">
      {/* <div className="animate__animated animate__fadeInUp animate__delay-1s"> */}
      <div className="fade-in" ref={(el) => el && sectionsRef.current.push(el)}>
        <EventBanner event={events} />
      </div>

      {/* Featured Books Section */}
      {/* <div className="animate__animated animate__fadeInUp animate__delay-2s"> */}
      <div className="fade-in" ref={(el) => el && sectionsRef.current.push(el)}>
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

      {/* Featured Magazines Section */}
      {/* <div className="animate__animated animate__fadeInUp animate__delay-3s"> */}
      <div className="fade-in" ref={(el) => el && sectionsRef.current.push(el)}>
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

      {/* <div className="animate__animated animate__fadeInUp animate__delay-4s"> */}
      <div className="fade-in" ref={(el) => el && sectionsRef.current.push(el)}>
        <TopDonations />
      </div>

      {/* <div className="animate__animated animate__fadeInUp animate__delay-5s"> */}
      <div className="fade-in" ref={(el) => el && sectionsRef.current.push(el)}>
        <Container className="mt-5">
          <TestimonialsSection testimonials={testimonialsData} />
        </Container>
      </div>
    </Container>
  );
};

export default Home;
