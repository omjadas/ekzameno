import React, { useState } from "react";
import { Button, Container, Form, Navbar } from "react-bootstrap";
import { Link } from "react-router-dom";
import { AuthModal } from "../auth/authModal";
import { CreateSubjectModal } from "../subject/subjectModal";

export const Header = (): JSX.Element => {
  const [authModalShow, setAuthModalShow] = useState(false);
  const [createSubjectshow, setcreateSubjectShow] = useState(false);

  return (
    <Navbar bg="dark" variant="dark">
      <Container>
        <Link className="navbar-brand" to="/">Ekzameno</Link>
        <Button onClick={() => setcreateSubjectShow(true)}>
          Create Subject
        </Button>
        <CreateSubjectModal show={createSubjectshow} onHide={() => setcreateSubjectShow(false)} />
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
