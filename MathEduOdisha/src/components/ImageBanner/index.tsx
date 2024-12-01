import React from "react";
import Carousel from "react-bootstrap/Carousel";
import { EventType as event } from "../../types/eventType";
import "./imageBanner.css";

interface EventBannerProps {
  event: event[];
}

const ImageBanner: React.FC<EventBannerProps> = ({ event }) => {
  return (
    <Carousel data-bs-theme="dark">
      {event.map((event, idx) => (
        <Carousel.Item interval={1000} key={idx}>
          <img
            className="d-block w-100 event-banner d-flex align-items-center text-white"
            src={event.imageURL}
            alt="First slide"
          />
          {/* <Carousel.Caption>
            <h3>{event.title}</h3>
            <p>{event.description}</p>
          </Carousel.Caption> */}
        </Carousel.Item>
      ))}
    </Carousel>
  );
};

export default ImageBanner;
