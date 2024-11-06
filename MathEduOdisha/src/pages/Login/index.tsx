// src/pages/LoginPage.tsx
import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import LoginForm from "../../components/Forms/LoginForm";
import { useAuth } from "../../hooks/useAuth";

const LoginPage: React.FC = () => {
  const { login, error } = useAuth();

  return (
    <Container className="py-5">
      <Row className="justify-content-center">
        <Col md={6} sm={10} xs={12}>
          <h2 className="text-center mb-4">Log In</h2>
          <LoginForm onSubmit={login} error={error} />
          <p className="text-center mt-3">
            Don't have an account? <a href="/signup">Sign Up</a>
          </p>
        </Col>
      </Row>
    </Container>
  );
};

export default LoginPage;
