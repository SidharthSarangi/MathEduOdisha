// src/pages/SignupPage.tsx
import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import SignupForm from "../../components/Forms/SignupForm";
import { useAuth } from "../../hooks/useAuth";

const SignupPage: React.FC = () => {
  const { signup, error } = useAuth();

  return (
    <Container className="py-5">
      <Row className="justify-content-center">
        <Col md={6} sm={10} xs={12}>
          <h2 className="text-center mb-4">Sign Up</h2>
          <SignupForm onSubmit={signup} error={error} />
          <p className="text-center mt-3">
            Already have an account? <a href="/login">Log In</a>
          </p>
        </Col>
      </Row>
    </Container>
  );
};

export default SignupPage;
