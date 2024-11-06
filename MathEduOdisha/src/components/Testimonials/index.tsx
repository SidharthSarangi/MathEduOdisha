import React, { useRef } from "react";
import { Button, Card } from "react-bootstrap";
import { TestimonialType } from "../../types/testimonialType";
import "./testimonials.css";

interface TestimonialCarouselProps {
  testimonials: TestimonialType[];
}

const TestimonialsSection: React.FC<TestimonialCarouselProps> = ({
  testimonials,
}) => {
  const scrollRef = useRef<HTMLDivElement>(null);

  const scrollLeft = () => {
    if (scrollRef.current) {
      scrollRef.current.scrollBy({ left: -300, behavior: "smooth" });
    }
  };

  const scrollRight = () => {
    if (scrollRef.current) {
      scrollRef.current.scrollBy({ left: 300, behavior: "smooth" });
    }
  };

  return (
    <div className="position-relative">
      <h2 className="text-center mb-4">What People Say About Us</h2>
      <div className="d-flex align-items-center">
        <Button variant="secondary" onClick={scrollLeft} className="me-2">
          ←
        </Button>
        <div className="scroll-container" ref={scrollRef}>
          {testimonials.map((testimonial, index) => (
            <Card key={index} className="testimonial-card mx-2 shadow-sm">
              <Card.Body>
                <Card.Text className="text-muted fst-italic">
                  "{testimonial.quote}"
                </Card.Text>
                <Card.Subtitle className="fw-bold mt-3">
                  ~{testimonial.author}
                </Card.Subtitle>
              </Card.Body>
            </Card>
          ))}
        </div>
        <Button variant="secondary" onClick={scrollRight} className="ms-2">
          →
        </Button>
      </div>
    </div>
  );
};

export default TestimonialsSection;
