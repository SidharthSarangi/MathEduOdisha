import React, { useEffect, useState } from "react";
import { Navbar, Nav, Container, Dropdown, Button } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { FaUserCircle } from "react-icons/fa";

const NavigationBar: React.FC = () => {
  const isAuthenticated = false; // Update this based on auth state in your app
  const navigate = useNavigate();
  const [showAboutDropdown, setShowAboutDropdown] = useState(false);
  const [showPublicationDropdown, setShowPublicationDropdown] = useState(false);
  const [showBookDropdown, setShowBookDropdown] = useState(false);

  const [language, setLanguage] = useState<string>("en"); // Default language

  // Function to toggle language
  const toggleLanguage = () => {
    setLanguage((prevLang) => (prevLang === "en" ? "or" : "en"));
  };

  useEffect(() => {
    // Load Google Translate script if it doesn't already exist
    if (!document.querySelector("#google-translate-script")) {
      const googleTranslateScript = document.createElement("script");
      googleTranslateScript.id = "google-translate-script";
      googleTranslateScript.src = `https://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit`;
      googleTranslateScript.async = true;
      document.body.appendChild(googleTranslateScript);
    }

    // Initialize Google Translate when the language changes
    window.googleTranslateElementInit = () => {
      new window.google.translate.TranslateElement(
        {
          pageLanguage: "en",
          includedLanguages: "en,or",
          layout: window.google.translate.TranslateElement.InlineLayout.SIMPLE,
        },
        "google_translate_element"
      );
    };
  }, [language]); // Trigger effect when the language state changes

  const handleAboutUsDropdownClick = () => {
    navigate("/about-us"); // Redirect to About Us
  };

  const handlePublicationDropdownClick = () => {
    navigate("/publication");
  };

  const handleBookDropdownClick = () => {
    navigate("/books");
  };

  return (
    <Navbar bg="light" expand="lg" className="shadow-sm">
      <Container>
        <Navbar.Brand as={Link} to="/">
          OCMSE
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="navbar-nav" />
        <Navbar.Collapse id="navbar-nav">
          <Nav className="me-auto">
            <Dropdown
              as={Nav.Item}
              className="about-dropdown"
              onMouseEnter={() => setShowAboutDropdown(true)} // Show on hover
              onMouseLeave={() => setShowAboutDropdown(false)} // Hide on mouse leave
              show={showAboutDropdown}
            >
              <Dropdown.Toggle
                as={Nav.Link}
                className="nav-link"
                onClick={handleAboutUsDropdownClick}
              >
                About Us
              </Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item as={Link} to="/about-ocmse">
                  About OCMSE
                </Dropdown.Item>
                <Dropdown.Item as={Link} to="/trustees">
                  Trustees
                </Dropdown.Item>
                <Dropdown.Item as={Link} to="/advisory-board">
                  Advisory Board
                </Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>

            <Dropdown
              as={Nav.Item}
              className="about-dropdown"
              onMouseEnter={() => setShowPublicationDropdown(true)} // Show on hover
              onMouseLeave={() => setShowPublicationDropdown(false)} // Hide on mouse leave
              show={showPublicationDropdown}
            >
              <Dropdown.Toggle
                as={Nav.Link}
                className="nav-link"
                onClick={handlePublicationDropdownClick}
              >
                Publication
              </Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item as={Link} to="/books">
                  Books
                </Dropdown.Item>
                <Dropdown.Item as={Link} to="/leelavati">
                  Leelavati
                </Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>

            <Dropdown
              as={Nav.Item}
              className="about-dropdown"
              onMouseEnter={() => setShowBookDropdown(true)} // Show on hover
              onMouseLeave={() => setShowBookDropdown(false)} // Hide on mouse leave
              show={showBookDropdown}
            >
              <Dropdown.Toggle
                as={Nav.Link}
                className="nav-link"
                onClick={handleBookDropdownClick}
              >
                Books
              </Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item as={Link} to="/books/olympaid">
                  Olympaids
                </Dropdown.Item>
                <Dropdown.Item as={Link} to="/books/history">
                  History
                </Dropdown.Item>
                <Dropdown.Item as={Link} to="/books/general">
                  General
                </Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>

            <Nav.Link as={Link} to="/events">
              Events
            </Nav.Link>
            <Nav.Link as={Link} to="/author">
              Authors
            </Nav.Link>
          </Nav>

          <Nav>
            <Button
              variant="outline-warning"
              onClick={toggleLanguage}
              className="ml-3"
            >
              {/* {language === "en" ? "Switch to Odia" : "Switch to English"} */}
              <div id="google_translate_element"></div>
            </Button>

            {/* Google Translate Element */}

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
