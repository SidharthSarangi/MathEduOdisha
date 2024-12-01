// src/components/ScrollableEvents.tsx
import React from "react";
import "./events.css";

const Events: React.FC = () => {
  const events = [
    { id: 1, title: "Book Fair 2024", date: "March 12, 2024" },
    { id: 2, title: "Author Meetup", date: "April 8, 2024" },
    { id: 3, title: "Writing Workshop", date: "May 20, 2024" },
    { id: 4, title: "Literary Awards", date: "June 15, 2024" },
    { id: 5, title: "Poetry Slam Night", date: "July 10, 2024" },
    { id: 6, title: "Math Guru", date: "August 5, 2024" },
    { id: 7, title: "Equation Fusion", date: "September 16, 2024" },
  ];

  return (
    <div className="scrollable-events">
      <h2>Upcoming Events</h2>
      <div className="events-list">
        {events.map((event) => (
          <div key={event.id} className="event-card">
            <h3>{event.title}</h3>
            <p>{event.date}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Events;
