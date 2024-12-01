import React, { useEffect, useRef, useState } from "react";
import { Col, Container, Row, Spinner } from "react-bootstrap";
import { fetchEvents } from "../../api/events";
import { EventType as Event } from "../../types/eventType";
import ImageBanner from "../../components/ImageBanner";
import TestimonialsSection from "../../components/Testimonials";
import "./home.css";
import AboutSection from "../../components/AboutSection";
import Events from "../../components/Events";

const Home: React.FC = () => {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState<Boolean>(false);

  const sectionsRef = useRef<HTMLDivElement[]>([]);

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
        <ImageBanner event={events} />
      </div>

      <div className="about-events-container">
        <AboutSection />
        <Events />
      </div>

      {/* Featured Books Section */}

      {/* Featured Magazines Section */}

      {/* <div className="animate__animated animate__fadeInUp animate__delay-4s"> */}
      {/* <div className="fade-in" ref={(el) => el && sectionsRef.current.push(el)}>
        <TopDonations />
      </div> */}

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
