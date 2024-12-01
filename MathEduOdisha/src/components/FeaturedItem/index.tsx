import React from "react";
import { Card } from "react-bootstrap";

interface FeaturedItemProps {
  title: string;
  description: string;
  imageUrl: string;
  link: string;
}

const FeaturedItem: React.FC<FeaturedItemProps> = ({
  title,
  description,
  imageUrl,
  link,
}) => {
  return (
    <Card
      className="featured-item-card shadow-sm mx-2 my-3"
      style={{ maxWidth: "300px" }}
    >
      <Card.Img
        variant="top"
        src={imageUrl}
        alt={title}
        style={{ height: "200px", objectFit: "cover" }}
      />
      <Card.Body>
        <Card.Title>{title}</Card.Title>
        <Card.Text>{description.slice(0, 80)}...</Card.Text>
        <a href={link} className="btn btn-dark btn-sm">
          Read More
        </a>
      </Card.Body>
    </Card>
  );
};

export default FeaturedItem;
