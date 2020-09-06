import React, { useState } from "react";
import { Button, Container, Form, Navbar } from "react-bootstrap";
import { Link } from "react-router-dom";
import { AuthModal } from "../auth/authModal";

export const Header = (): JSX.Element => {
  const [authModalShow, setAuthModalShow] = useState(false);

  return (
    <Navbar bg="dark" variant="dark">
      <Container>
        <Link className="navbar-brand" to="/">Ekzameno</Link>
        <Form className="ml-auto" inline>
          <Button
            variant="outline-info"
            onClick={() => setAuthModalShow(true)}>
            Sign In
          </Button>
        </Form>
        <AuthModal show={authModalShow} onHide={() => setAuthModalShow(false)} />
      </Container>
    </Navbar>
  );
};
