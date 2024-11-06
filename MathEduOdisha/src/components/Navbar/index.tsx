import React from "react";
import { Navbar, Nav, Container, Dropdown } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaUserCircle } from "react-icons/fa";

const NavigationBar: React.FC = () => {
  const isAuthenticated = false; // Update this based on auth state in your app

  return (
    <Navbar bg="light" expand="lg" className="shadow-sm">
      <Container>
        <Navbar.Brand as={Link} to="/">
          OCMSE
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="navbar-nav" />
        <Navbar.Collapse id="navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/books">
              Books
            </Nav.Link>
            <Nav.Link as={Link} to="/magazines">
              Magazines
            </Nav.Link>
            <Nav.Link as={Link} to="/events">
              Events
            </Nav.Link>
            <Nav.Link as={Link} to="/author">
              Authors
            </Nav.Link>
          </Nav>
          <Nav>
            <Dropdown align="end">
              <Dropdown.Toggle
                variant="light"
                className="d-flex align-items-center border-0"
              >
                <FaUserCircle size={28} />
                <span>{isAuthenticated ? `Hi,` : "Account"}</span>
              </Dropdown.Toggle>

              <Dropdown.Menu>
                {!isAuthenticated ? (
                  <>
                    <Dropdown.Item as={Link} to="/login">
                      Log In
                    </Dropdown.Item>
                    <Dropdown.Item as={Link} to="/signup">
                      Sign Up
                    </Dropdown.Item>
                  </>
                ) : (
                  <Dropdown.Item as={Link} to="/logout">
                    Log Out
                  </Dropdown.Item>
                )}
              </Dropdown.Menu>
            </Dropdown>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default NavigationBar;
