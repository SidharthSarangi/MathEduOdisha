// src/components/TopDonations.tsx
import React, { useEffect, useState } from "react";
import { Card, Row, Col, Container } from "react-bootstrap";
import { fetchTopDonations } from "../../services/donations";
import { Donation } from "../../types/donationType";
import "./topdonations.css";

const TopDonations: React.FC = () => {
  const [donations, setDonations] = useState<Donation[]>([]);

  useEffect(() => {
    const loadDonations = async () => {
      const donationsData = await fetchTopDonations();
      setDonations(donationsData);
    };
    loadDonations();
  }, []);

  return (
    <div className="top-donations-section">
      <Container className="my-5">
        <h2 className="text-center mb-4">Top Donations</h2>
        <Row xs={1} sm={2} md={3} lg={4} className="g-4">
          {donations.map((donation) => (
            <Col key={donation.id}>
              <Card className="donation-card shadow-sm h-100">
                <Card.Body>
                  <Card.Title className="donation-donor-name">
                    {donation.donorName}
                  </Card.Title>
                  <Card.Text className="donation-amount">
                    <strong>Amount:</strong> ₹{donation.amount.toFixed(2)}
                  </Card.Text>
                  <Card.Text className="donation-date">
                    <small>
                      {new Date(donation.date).toLocaleDateString()}
                    </small>
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </Container>
    </div>
  );
};

export default TopDonations;
