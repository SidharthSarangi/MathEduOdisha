import React from "react";
import { Container, Row, Col, Form, Button } from "react-bootstrap";
import { FaFacebook, FaTwitter, FaInstagram, FaLinkedin } from "react-icons/fa";
import "./footer.css";

const Footer: React.FC = () => {
  return (
    <footer className="footer py-4">
      <Container>
        <Row className="text-center text-md-start">
          {/* Newsletter Section */}
          <Col md={4} className="mb-4">
            <h5 className="fw-bold">Subscribe to our Newsletter</h5>
            <p className="text-muted">
              Get updates on new books, events, and special offers directly to
              your inbox.
            </p>
            <Form>
              <Form.Group controlId="subscribeEmail" className="d-flex">
                <Form.Control
                  type="email"
                  placeholder="Enter your email"
                  className="me-2"
                  style={{ borderRadius: "20px" }}
                />
                <Button
                  variant="primary"
                  type="submit"
                  style={{ borderRadius: "20px" }}
                >
                  Subscribe
                </Button>
              </Form.Group>
            </Form>
          </Col>

          {/* Quick Links Section */}
          <Col md={4} className="mb-4">
            <h5 className="fw-bold">Quick Links</h5>
            <ul className="list-unstyled">
              <li>
                <a href="/books" className="text-decoration-none text-muted">
                  Books
                </a>
              </li>
              <li>
                <a
                  href="/magazines"
                  className="text-decoration-none text-muted"
                >
                  Magazines
                </a>
              </li>
              <li>
                <a href="/events" className="text-decoration-none text-muted">
                  Events
                </a>
              </li>
              <li>
                <a href="/trust" className="text-decoration-none text-muted">
                  Trust
                </a>
              </li>
              <li>
                <a
                  href="/advisory-board"
                  className="text-decoration-none text-muted"
                >
                  Advisory Board
                </a>
              </li>
            </ul>
          </Col>

          {/* Contact & Social Links */}
          <Col md={4} className="mb-4">
            <h5 className="fw-bold">Follow Us</h5>
            <p className="text-muted">
              Join us on social media for updates and more!
            </p>
            <div className="social-icons">
              <a href="https://facebook.com" className="text-muted me-3">
                <FaFacebook size={24} />
              </a>
              <a href="https://twitter.com" className="text-muted me-3">
                <FaTwitter size={24} />
              </a>
              <a href="https://instagram.com" className="text-muted me-3">
                <FaInstagram size={24} />
              </a>
              <a href="https://linkedin.com" className="text-muted">
                <FaLinkedin size={24} />
              </a>
            </div>
          </Col>
        </Row>

        <hr className="my-4" />

        {/* Footer Bottom Section */}
        <Row>
          <Col className="text-center text-muted">
            <p className="mb-0">
              © {new Date().getFullYear()} Your Own Odisha Math Book Website.
              All rights reserved. Made with ❤️
            </p>
          </Col>
        </Row>
      </Container>
    </footer>
  );
};

export default Footer;
